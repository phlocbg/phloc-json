/**
 * Copyright (C) 2006-2015 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.json.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.IJSONPropertyValueNotParsable;
import com.phloc.json.JSONUtil;
import com.phloc.json.impl.value.AbstractJSONPropertyValue;
import com.phloc.json.impl.value.AbstractJSONPropertyValueNumeric;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBigInteger;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueDouble;
import com.phloc.json.impl.value.JSONPropertyValueFunction;
import com.phloc.json.impl.value.JSONPropertyValueFunctionPrebuild;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueJSONObject;
import com.phloc.json.impl.value.JSONPropertyValueKeyword;
import com.phloc.json.impl.value.JSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueLong;
import com.phloc.json.impl.value.JSONPropertyValueString;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a JSON object having a map of named JSON properties
 *
 * @author Boris Gregorcic, philip
 */
@SuppressWarnings ("deprecation")
public class JSONObject extends AbstractJSONPropertyValue <IJSONObject> implements IJSONObject
{

  private static final long serialVersionUID = 6726669796398394782L;

  private static final Logger LOG = LoggerFactory.getLogger (JSONObject.class);

  private final Map <String, IJSONProperty <?>> m_aProperties = new LinkedHashMap <String, IJSONProperty <?>> ();
  private final List <JSONObject> m_aParents = new CopyOnWriteArrayList <> ();
  private final List <JSONObject> m_aChildren = new CopyOnWriteArrayList <> ();

  /**
   * Default Ctor. Handle with care as it by default sets a <code>null</code>
   * value which is most probable be crashing somewhere inside this class, if no
   * data is provided afterwards!
   */
  public JSONObject ()
  {
    super ();
    JSONStatistics.getInstance ().onObjectCreated ();
  }

  public JSONObject (@Nonnull final Iterable <? extends IJSONProperty <?>> aProperties)
  {
    this (aProperties, ECloneStategy.INHERIT);
  }

  public JSONObject (@Nonnull final Iterable <? extends IJSONProperty <?>> aProperties,
                     final ECloneStategy eCloneStrategy)
  {
    super ();
    if (aProperties == null)
    {
      throw new NullPointerException ("properties"); //$NON-NLS-1$
    }
    for (final IJSONProperty <?> aProperty : aProperties)
    {
      setProperty (aProperty, eCloneStrategy);
    }
    JSONStatistics.getInstance ().onObjectCreated ();
  }

  /**
   * Override since otherwise JSONObjects might return null for certain
   * constructors
   *
   * @return this
   */
  @Override
  @Nonnull
  public IJSONObject getData ()
  {
    return this;
  }

  @Override
  @Nonnull
  public JSONObject setProperty (@Nonnull final IJSONProperty <?> aProperty)
  {
    return setProperty (aProperty, ECloneStategy.INHERIT);
  }

  @Nonnull
  private JSONObject setProperty (@Nonnull final IJSONProperty <?> aProperty, final ECloneStategy eCloneStrategy)
  {
    if (aProperty == null)
    {
      throw new NullPointerException ("property"); //$NON-NLS-1$
    }

    final boolean bCloneProperties = JSONSettings.getInstance ().isCloneProperties ();
    final boolean bCloneProperty = eCloneStrategy == ECloneStategy.FORCE ||
                                   eCloneStrategy != ECloneStategy.AVOID && bCloneProperties;

    final boolean bDetectSideEffects = JSONSettings.getInstance ().isDetectSideEffects ();
    final boolean bDetectCycles = JSONSettings.getInstance ().isDetectCycles ();

    List <JSONObject> aObjectsInValue = null;
    if (!bCloneProperty && bDetectCycles)
    {
      aObjectsInValue = getObjectsInValue (aProperty);
      for (final IJSONObject aChild : aObjectsInValue)
      {
        checkCycle (aChild);
      }
      if (bDetectSideEffects)
      {
        final IJSONObject aSharedAncestor = getAncestorWithMultipleParentsRecursive ();
        if (aSharedAncestor != null && LOG.isErrorEnabled ())
        {
          LOG.error ("Trying to assign property '{}' in object {} while this object is shared in ancestor {}!", //$NON-NLS-1$
                     aProperty.getName (),
                     String.valueOf (this.hashCode ()),
                     String.valueOf (aSharedAncestor.hashCode ()));
        }
      }
    }
    this.m_aProperties.put (aProperty.getName (), bCloneProperty ? aProperty.getClone () : aProperty);
    if (bDetectCycles && aObjectsInValue != null)
    {
      for (final JSONObject aChild : aObjectsInValue)
      {
        if (aChild.hasParents () && bDetectSideEffects && LOG.isWarnEnabled ())
        {
          LOG.warn ("Trying to assign property '{}' in object {} with a value containing object references already used in other parents (THIS MAY CAUSE SIDE EFFECTS if you change the object later). Make sure to explicitly clone objects in such cases or enable property cloning in the JSONSettings!", //$NON-NLS-1$
                    aProperty.getName (),
                    String.valueOf (this.hashCode ()));
        }
        aChild.m_aParents.add (this);
        this.m_aChildren.add (aChild);
      }
    }
    return this;
  }

  @Nonnull
  private static List <JSONObject> getObjectsInValue (@Nonnull final IJSONProperty <?> aProperty)
  {
    final List <JSONObject> aObjects = ContainerHelper.newList ();
    final IJSONPropertyValue <?> aValue = aProperty.getValue ();
    if (aValue instanceof JSONObject)
    {
      aObjects.add ((JSONObject) aValue);
    }
    else
      if (aValue instanceof IJSONPropertyValueList <?>)
      {
        for (final Object aListElem : ((IJSONPropertyValueList <?>) aValue).getDataValues ())
        {
          if (aListElem instanceof JSONObject)
          {
            aObjects.add ((JSONObject) aListElem);
          }
        }
      }
    return aObjects;
  }

  @Override
  @Nullable
  public IJSONProperty <?> getProperty (@Nullable final String sName)
  {
    return getProperty (sName, false);
  }

  @Override
  @Nullable
  public IJSONProperty <?> getProperty (@Nullable final String sName, final boolean bReturnNull)
  {
    final IJSONProperty <?> aProperty = this.m_aProperties.get (sName);
    if (aProperty == null)
    {
      return null;
    }
    if (this.isNull (sName) && !bReturnNull)
    {
      return null;
    }
    return this.m_aProperties.get (sName);
  }

  @Override
  @Nullable
  public Object getPropertyValueData (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    return aValue == null ? null : aValue.getData ();
  }

  @Override
  @Nullable
  public Object getNumericProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof AbstractJSONPropertyValueNumeric)
    {
      return aValue.getData ();
    }
    return null;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllPropertyNames ()
  {
    return ContainerHelper.newOrderedSet (this.m_aProperties.keySet ());
  }

  @Nullable
  private IJSONPropertyValue <?> getPropertyValueInternal (@Nullable final String sName)
  {
    final IJSONProperty <?> aProperty = this.m_aProperties.get (sName);
    return aProperty == null ? null : aProperty.getValue ();
  }

  // we need to differentiate here the case of set boolean values from the case
  // the property is not found or of wrong type (null). Un-boxing is prevented
  // by PMD anyway and the method is annotated as @Nullable
  @Override
  @Nullable
  @SuppressFBWarnings ("NP_BOOLEAN_RETURN_NULL")
  public Boolean getBooleanProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBoolean)
    {
      return ((JSONPropertyValueBoolean) aValue).getData ();
    }
    return null;
  }

  @Override
  public boolean getBoolProperty (@Nullable final String sName, final boolean bDefault)
  {
    final Boolean aVal = getBooleanProperty (sName);
    return aVal == null ? bDefault : aVal.booleanValue ();
  }

  @Override
  @Nonnull
  public JSONObject setBooleanProperty (@Nonnull final String sName, final boolean bDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (bDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setBooleanProperty (@Nonnull final String sName, @Nullable final Boolean aDataValue)
  {
    if (aDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (aDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nullable
  public Double getDoubleProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof AbstractJSONPropertyValueNumeric)
    {
      return Double.valueOf (((AbstractJSONPropertyValueNumeric <?>) aValue).getData ().doubleValue ());
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setDoubleProperty (@Nonnull final String sName, final double nDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (nDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setDoubleProperty (@Nonnull final String sName, @Nullable final Double aDataValue)
  {
    if (aDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (aDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nullable
  public Integer getIntegerProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof AbstractJSONPropertyValueNumeric)
    {
      return Integer.valueOf (((AbstractJSONPropertyValueNumeric <?>) aValue).getData ().intValue ());
    }
    return null;
  }

  @Override
  public int getIntProperty (@Nullable final String sName, final int nDefault)
  {
    final Integer aVal = getIntegerProperty (sName);
    return aVal == null ? nDefault : aVal.intValue ();
  }

  @Override
  @Nonnull
  public Integer getIntegerPropertyNonNull (@Nullable final String sName)
  {
    final Integer aValue = getIntegerProperty (sName);
    if (aValue == null)
    {
      throw new NullPointerException ("No Integer value available for property '" + sName + "'!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return aValue;
  }

  @Override
  @Nonnull
  public JSONObject setIntegerProperty (@Nonnull final String sName, final int nDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (nDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setIntegerProperty (@Nonnull final String sName, @Nullable final Integer aDataValue)
  {
    if (aDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (aDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nullable
  public Long getLongProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof AbstractJSONPropertyValueNumeric)
    {
      return Long.valueOf (((AbstractJSONPropertyValueNumeric <?>) aValue).getData ().longValue ());
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setLongProperty (@Nonnull final String sName, final long nDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueLong (nDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setLongProperty (@Nonnull final String sName, @Nullable final Long aDataValue)
  {
    if (aDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueLong (aDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nullable
  public String getKeywordProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueKeyword)
    {
      return ((JSONPropertyValueKeyword) aValue).getData ();
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setKeywordProperty (@Nonnull final String sName, @Nullable final String sDataValue)
  {
    if (sDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueKeyword (sDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setFunctionProperty (@Nonnull final String sName,
                                         @Nonnull final String sBody,
                                         @Nullable final String... aParams)
  {
    return setProperty (JSONProperty.create (sName,
                                             new JSONPropertyValueFunction (sBody, aParams),
                                             ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setFunctionPrebuildProperty (@Nonnull final String sName, @Nonnull final String sFunctionCode)
  {
    return setProperty (JSONProperty.create (sName,
                                             new JSONPropertyValueFunctionPrebuild (sFunctionCode),
                                             ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @SuppressWarnings ("unchecked")
  public <I> List <I> getListProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
    {
      return (List <I>) ((IJSONPropertyValueList <?>) aValue).getDataValues ();
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setListProperty (@Nonnull final String sName, @Nonnull final IJSONPropertyValueList <?> aList)
  {
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.INHERIT), ECloneStategy.AVOID);
  }

  @Override
  @Nullable
  public IJSONObject getObjectProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof IJSONObject)
    {
      return (IJSONObject) aValue;
    }
    if (aValue instanceof JSONPropertyValueJSONObject)
    {
      return ((JSONPropertyValueJSONObject) aValue).getData ();
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setObjectProperty (@Nonnull final String sName, @Nullable final IJSONObject aObject)
  {
    if (aObject == null)
    {
      return setNull (sName);
    }
    setProperty (JSONProperty.create (sName, aObject, ECloneStategy.INHERIT), ECloneStategy.AVOID);
    return this;
  }

  private boolean hasParents ()
  {
    return !this.m_aParents.isEmpty ();
  }

  private IJSONObject getAncestorWithMultipleParentsRecursive ()
  {
    if (this.m_aParents.size () > 1)
    {
      return this;
    }
    for (final JSONObject aParent : this.m_aParents)
    {
      final IJSONObject aA = aParent.getAncestorWithMultipleParentsRecursive ();
      if (aA != null)
      {
        return aA;
      }
    }
    return null;
  }

  private List <JSONObject> getParentsRecursive ()
  {
    final List <JSONObject> aResult = ContainerHelper.newList ();
    for (final JSONObject aParent : this.m_aParents)
    {
      aResult.add (aParent);
      aResult.addAll (aParent.getParentsRecursive ());
    }
    return aResult;
  }

  public List <JSONObject> getChildrenRecursive ()
  {
    final List <JSONObject> aResult = ContainerHelper.newList ();
    for (final JSONObject aChild : this.m_aChildren)
    {
      aResult.add (aChild);
      aResult.addAll (aChild.getChildrenRecursive ());
    }
    return aResult;
  }

  private List <JSONObject> getWithAncestors ()
  {
    final List <JSONObject> aAncestors = getParentsRecursive ();
    aAncestors.add (this);
    return aAncestors;
  }

  private List <JSONObject> getWithDescendants ()
  {
    final List <JSONObject> aDescendants = getChildrenRecursive ();
    aDescendants.add (this);
    return aDescendants;
  }

  private void checkCycle (final IJSONObject aChild)
  {
    if (this == aChild)
    {
      throw new IllegalArgumentException ("Circle detected: Unable to set object reference from A to B when A==B!"); //$NON-NLS-1$
    }
    if (aChild instanceof JSONObject)
    {
      final List <JSONObject> aAncestors = getWithAncestors ();
      final List <JSONObject> aDescendents = ((JSONObject) aChild).getWithDescendants ();
      final List <JSONObject> aCycles = getIntersectedByIdentity (aAncestors, aDescendents);
      if (!ContainerHelper.isEmpty (aCycles))
      {
        throw new IllegalArgumentException ("Circle detected: Unable to set object reference from A to B when B or one of its descendants is the same as A or one of its ancestors!"); //$NON-NLS-1$
      }
    }
  }

  private static List <JSONObject> getIntersectedByIdentity (final List <JSONObject> aObjectsA,
                                                             final List <JSONObject> aObjectsB)
  {
    final List <JSONObject> aIntersect = ContainerHelper.newList ();
    for (final JSONObject aA : aObjectsA)
    {
      for (final IJSONObject aB : aObjectsB)
      {
        if (aA == aB)
        {
          aIntersect.add (aA);
        }
      }
    }
    return aIntersect;
  }

  @Override
  @Nullable
  public String getStringProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueString)
    {
      return ((JSONPropertyValueString) aValue).getData ();
    }
    return null;
  }

  @Override
  public String getStringPropertyNonEmpty (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
    {
      throw new IllegalArgumentException ("sName must not be null or empty!"); //$NON-NLS-1$
    }
    final String sValue = getStringProperty (sName);
    if (StringHelper.hasNoText (sValue))
    {
      throw new IllegalArgumentException ("Value for property '" + sName + "' must not be empty or null!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return sValue;
  }

  @Override
  @Nonnull
  public JSONObject setStringProperty (@Nonnull final String sName, @Nullable final String sDataValue)
  {
    if (sDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueString (sDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public IJSONObject setStringProperties (@Nonnull final Map <String, String> aMap)
  {
    for (final Map.Entry <String, String> aEntry : aMap.entrySet ())
    {
      setStringProperty (aEntry.getKey (), aEntry.getValue ());
    }
    return this;
  }

  @Override
  @Nullable
  public BigInteger getBigIntegerProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof AbstractJSONPropertyValueNumeric)
    {
      return BigInteger.valueOf (((AbstractJSONPropertyValueNumeric <?>) aValue).getData ().longValue ());
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setBigIntegerProperty (@Nonnull final String sName, @Nullable final BigInteger aDataValue)
  {
    if (aDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBigInteger (aDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nullable
  public BigDecimal getBigDecimalProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof AbstractJSONPropertyValueNumeric)
    {
      return BigDecimal.valueOf (((AbstractJSONPropertyValueNumeric <?>) aValue).getData ().doubleValue ());
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setBigDecimalProperty (@Nonnull final String sName, @Nullable final BigDecimal aDataValue)
  {
    if (aDataValue == null)
    {
      return setNull (sName);
    }
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBigDecimal (aDataValue), ECloneStategy.AVOID),
                        ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setProperty (@Nonnull final String sName, @Nullable final Object aValue)
  {
    // Default: no type converter
    return setProperty (sName, aValue, false);
  }

  @Override
  @Nonnull
  public JSONObject setProperty (@Nonnull final String sName,
                                 @Nullable final Object aValue,
                                 final boolean bUseTypeConverter)
  {
    if (aValue == null)
    {
      return setNull (sName);
    }
    if (aValue instanceof IJSONObject)
    {
      return setObjectProperty (sName, (IJSONObject) aValue);
    }
    if (aValue instanceof IJSONPropertyValue <?>)
    {
      return setProperty (JSONProperty.create (sName, (IJSONPropertyValue <?>) aValue, ECloneStategy.INHERIT),
                          ECloneStategy.AVOID);
    }
    if (aValue instanceof Boolean)
    {
      return setBooleanProperty (sName, (Boolean) aValue);
    }
    if (aValue instanceof BigInteger)
    {
      return setBigIntegerProperty (sName, (BigInteger) aValue);
    }
    if (aValue instanceof BigDecimal)
    {
      return setBigDecimalProperty (sName, (BigDecimal) aValue);
    }
    if (aValue instanceof Double)
    {
      return setDoubleProperty (sName, (Double) aValue);
    }
    if (aValue instanceof Integer)
    {
      return setIntegerProperty (sName, (Integer) aValue);
    }
    if (aValue instanceof Long)
    {
      return setLongProperty (sName, (Long) aValue);
    }
    if (aValue instanceof String)
    {
      return setStringProperty (sName, (String) aValue);
    }
    // Unknown type -> use type converter?
    String sValue;
    if (bUseTypeConverter)
    {
      sValue = TypeConverter.convertIfNecessary (aValue, String.class);
    }
    else
    {
      LOG.warn ("Setting property of type " + //$NON-NLS-1$
                aValue.getClass ().getName () +
                " as String without TypeConverter!"); //$NON-NLS-1$
      sValue = String.valueOf (aValue);
    }
    return setStringProperty (sName, sValue);
  }

  @Override
  @Nullable
  public List <? extends IJSONPropertyValue <?>> getListValues (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
    {
      return ((IJSONPropertyValueList <?>) aValue).getValues ();
    }
    return null;
  }

  @Override
  @Nonnull
  public JSONObject setObjectListProperty (@Nonnull final String sName,
                                           @Nonnull final Iterable <? extends IJSONObject> aObjectList)
  {
    if (ContainerHelper.containsAnyNullElement (aObjectList))
    {
      return setMixedListProperty (sName, aObjectList);
    }
    final IJSONPropertyValueList <IJSONObject> aList = new JSONPropertyValueList <> ();
    for (final IJSONObject aObject : aObjectList)
    {
      aList.addValue (aObject);
    }
    setProperty (JSONProperty.create (sName, aList, ECloneStategy.INHERIT), ECloneStategy.AVOID);
    return this;
  }

  @Override
  @Nonnull
  public List <IJSONObject> getObjectListProperty (@Nullable final String sName)
  {
    final List <IJSONObject> aReturn = new ArrayList <> ();
    final List <?> aList = getListProperty (sName);
    if (aList != null)
    {
      for (final Object aValue : aList)
      {
        if (aValue instanceof IJSONObject)
        {
          aReturn.add ((IJSONObject) aValue);
        }
      }
    }
    return aReturn;
  }

  @Override
  @Nonnull
  public JSONObject setStringListProperty (@Nonnull final String sName, @Nonnull final Iterable <String> aStringList)
  {
    if (ContainerHelper.containsAnyNullElement (aStringList))
    {
      return setMixedListProperty (sName, aStringList);
    }
    final IJSONPropertyValueList <JSONPropertyValueString> aList = new JSONPropertyValueList <> ();
    for (final String sValue : aStringList)
    {
      aList.addValue (new JSONPropertyValueString (sValue));
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setMixedListProperty (@Nonnull final String sName, @Nonnull final Iterable <?> aValues)
  {
    final IJSONPropertyValueList <IJSONPropertyValue <?>> aList = new JSONPropertyValueList <> ();
    for (final Object aValue : aValues)
    {
      aList.addValue (JSONUtil.getJSONValue (aValue));
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.INHERIT), ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setIntegerListProperty (@Nonnull final String sName, @Nonnull final int [] aIntList)
  {
    final IJSONPropertyValueList <JSONPropertyValueInteger> aList = new JSONPropertyValueList <> ();
    for (final int nValue : aIntList)
    {
      aList.addValue (new JSONPropertyValueInteger (nValue));
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  public JSONObject setIntegerListProperty (@Nonnull final String sName, @Nonnull final List <Integer> aIntegerList)
  {
    if (ContainerHelper.containsAnyNullElement (aIntegerList))
    {
      return setMixedListProperty (sName, aIntegerList);
    }
    final IJSONPropertyValueList <JSONPropertyValueInteger> aList = new JSONPropertyValueList <> ();
    for (final Integer nValue : aIntegerList)
    {
      aList.addValue (new JSONPropertyValueInteger (nValue));
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setDoubleListProperty (@Nonnull final String sName, @Nonnull final double [] aDoubleList)
  {
    final IJSONPropertyValueList <JSONPropertyValueDouble> aList = new JSONPropertyValueList <> ();
    for (final double nValue : aDoubleList)
    {
      aList.addValue (new JSONPropertyValueDouble (nValue));
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  public JSONObject setDoubleListProperty (@Nonnull final String sName, @Nonnull final List <Double> aDoubleList)
  {
    if (ContainerHelper.containsAnyNullElement (aDoubleList))
    {
      return setMixedListProperty (sName, aDoubleList);
    }
    final IJSONPropertyValueList <JSONPropertyValueDouble> aList = new JSONPropertyValueList <> ();
    for (final Double nValue : aDoubleList)
    {
      aList.addValue (new JSONPropertyValueDouble (nValue));
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  @Deprecated
  @Nonnull
  public JSONObject setListOfListProperty (@Nonnull final String sName,
                                           @Nonnull final Iterable <? extends Iterable <String>> aListOfList)
  {
    return setListOfStringListProperty (sName, aListOfList);
  }

  @Override
  @SuppressWarnings ("unchecked")
  @Nonnull
  public JSONObject setListOfListPropertyMixed (@Nonnull final String sName,
                                                @Nonnull final Iterable <? extends Iterable <Object>> aListOfList)
  {
    final JSONPropertyValueList <IJSONPropertyValueList <IJSONPropertyValue <Object>>> aList = new JSONPropertyValueList <> ();
    for (final Iterable <Object> aRow : aListOfList)
    {
      final IJSONPropertyValueList <IJSONPropertyValue <Object>> aRowList = new JSONPropertyValueList <> ();
      for (final Object aCell : aRow)
      {
        aRowList.addValue ((IJSONPropertyValue <Object>) JSONUtil.getJSONValue (aCell));
      }
      aList.addValue (aRowList);
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  @Nonnull
  public JSONObject setListOfStringListProperty (@Nonnull final String sName,
                                                 @Nonnull final Iterable <? extends Iterable <String>> aListOfList)
  {
    final JSONPropertyValueList <IJSONPropertyValueList <JSONPropertyValueString>> aList = new JSONPropertyValueList <> ();
    for (final Iterable <String> aRow : aListOfList)
    {
      final IJSONPropertyValueList <JSONPropertyValueString> aRowList = new JSONPropertyValueList <> ();
      for (final String aCell : aRow)
      {
        aRowList.addValue (new JSONPropertyValueString (aCell));
      }
      aList.addValue (aRowList);
    }
    return setProperty (JSONProperty.create (sName, aList, ECloneStategy.AVOID), ECloneStategy.AVOID);
  }

  @Override
  public JSONObject set (@Nonnull @Nonempty final String sName, @Nullable final String sValue)
  {
    return set (sName, sValue, true);
  }

  @Override
  public JSONObject set (@Nonnull @Nonempty final String sName,
                         @Nullable final String sValue,
                         final boolean bEmitEmptyValue)
  {
    if (bEmitEmptyValue && sValue == null)
    {
      return setNull (sName);
    }
    if (sValue != null && (bEmitEmptyValue || StringHelper.hasText (sValue)))
    {
      setStringProperty (sName, sValue);
    }
    return this;
  }

  @Override
  public JSONObject set (@Nonnull @Nonempty final String sName, @Nullable final IJSONObject aValue)
  {
    return set (sName, aValue, true);
  }

  @Override
  public JSONObject set (@Nonnull @Nonempty final String sName,
                         @Nullable final IJSONObject aValue,
                         final boolean bEmitEmptyValue)
  {
    if (bEmitEmptyValue && aValue == null)
    {
      return setNull (sName);
    }
    if (aValue != null && (bEmitEmptyValue || !aValue.isEmpty ()))
    {
      setObjectProperty (sName, aValue);
    }
    return this;
  }

  @Override
  public JSONObject set (@Nonnull @Nonempty final String sName, @Nullable final List <IJSONObject> aValue)
  {
    return set (sName, aValue, true);
  }

  @Override
  public JSONObject set (@Nonnull @Nonempty final String sName,
                         @Nullable final List <IJSONObject> aValue,
                         final boolean bEmitEmptyValue)
  {
    if (bEmitEmptyValue && aValue == null)
    {
      return setNull (sName);
    }
    if (aValue != null && (bEmitEmptyValue || !aValue.isEmpty ()))
    {
      setObjectListProperty (sName, aValue);
    }
    return this;
  }

  @Override
  public JSONObject set (final @Nonnull @Nonempty String sName, final boolean bValue)
  {
    return setBooleanProperty (sName, bValue);
  }

  @Override
  public JSONObject set (final @Nonnull @Nonempty String sName, final int nValue)
  {
    return setIntegerProperty (sName, nValue);
  }

  @Override
  public JSONObject set (final @Nonnull @Nonempty String sName, final @Nullable Integer aValue)
  {
    return setIntegerProperty (sName, aValue);
  }

  @Override
  public JSONObject set (final @Nonnull @Nonempty String sName, final double nValue)
  {
    return setDoubleProperty (sName, nValue);
  }

  @Override
  public JSONObject set (final @Nonnull @Nonempty String sName, final @Nullable Double aValue)
  {
    return setDoubleProperty (sName, aValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final @Nullable String sValue)
  {
    return set (aName.getID (), sValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName,
                         final @Nullable String sValue,
                         final boolean bHandleEmptyValue)
  {
    return set (aName.getID (), sValue, bHandleEmptyValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final @Nullable IJSONObject aValue)
  {
    return set (aName.getID (), aValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName,
                         final @Nullable IJSONObject aValue,
                         final boolean bHandleEmptyValue)
  {
    return set (aName.getID (), aValue, bHandleEmptyValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final @Nullable List <IJSONObject> aValue)
  {
    return set (aName.getID (), aValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName,
                         final @Nullable List <IJSONObject> aValue,
                         final boolean bEmitEmptyValue)
  {
    return set (aName.getID (), aValue, bEmitEmptyValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final boolean bValue)
  {
    return set (aName.getID (), bValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final int nValue)
  {
    return set (aName.getID (), nValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final @Nullable Integer aValue)
  {
    return set (aName.getID (), aValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final double nValue)
  {
    return set (aName.getID (), nValue);
  }

  @Override
  public JSONObject set (final @Nonnull IHasID <String> aName, final @Nullable Double aValue)
  {
    return set (aName.getID (), aValue);
  }

  @Override
  public JSONObject setNull (final @Nonnull @Nonempty String sName)
  {
    return setKeywordProperty (sName, CJSONConstants.KEYWORD_NULL);
  }

  @Override
  public JSONObject setNull (final @Nonnull IHasID <String> aName)
  {
    return setNull (aName.getID ());
  }

  @Override
  public boolean isNull (final @Nonnull @Nonempty String sName)
  {
    return isNull (this.m_aProperties.get (sName));
  }

  private static boolean isNull (final IJSONProperty <?> aProp)
  {
    if (aProp == null)
    {
      return true;
    }
    final IJSONPropertyValue <?> aVal = aProp.getValue ();
    if (aVal instanceof JSONPropertyValueKeyword)
    {
      return CJSONConstants.KEYWORD_NULL.equals (((JSONPropertyValueKeyword) aVal).getData ());
    }
    return false;
  }

  @Override
  public boolean isNull (final @Nonnull IHasID <String> aName)
  {
    return isNull (aName.getID ());
  }

  @Override
  public boolean hasProperty (@Nonnull @Nonempty final String sName)
  {
    return this.m_aProperties.containsKey (sName);
  }

  @Override
  public boolean hasProperty (final @Nonnull IHasID <String> aName)
  {
    return hasProperty (aName.getID ());
  }

  @Override
  public boolean hasPropertyNonnull (@Nonnull @Nonempty final String sName)
  {
    return !isNull (sName);
  }

  @Override
  public boolean hasPropertyNonnull (final @Nonnull IHasID <String> aName)
  {
    return hasPropertyNonnull (aName.getID ());
  }

  @Override
  public void appendJSONString (@Nonnull final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    appendNewLine (aResult, bAlignAndIndent);
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.OBJECT_START);
    appendNewLine (aResult, bAlignAndIndent);

    final Set <String> aPropertyNames = getAllPropertyNames ();
    int nIndex = 0;
    for (final String sProperty : aPropertyNames)
    {
      final IJSONProperty <?> aProperty = getProperty (sProperty, true);
      if (aProperty == null)
      {
        LOG.warn ("Skipping property '{}' in generated JSON string as the corresponding property can no longer be resolved!", //$NON-NLS-1$
                  sProperty);
        continue;
      }
      aProperty.appendJSONString (aResult, bAlignAndIndent, nLevel + 1);
      if (nIndex < aPropertyNames.size () - 1)
      {
        aResult.append (CJSONConstants.TOKEN_SEPARATOR);
      }
      appendNewLine (aResult, bAlignAndIndent);
      nIndex++;
    }
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.OBJECT_END);
  }

  @Override
  @Nonnull
  public EChange removeProperty (@Nullable final String sName)
  {
    final IJSONProperty <?> aRemoved = this.m_aProperties.remove (sName);
    if (aRemoved == null)
    {
      return EChange.UNCHANGED;
    }
    if (!JSONSettings.getInstance ().isCloneProperties ())
    {
      final List <JSONObject> aObjectsInValue = getObjectsInValue (aRemoved);
      if (JSONSettings.getInstance ().isDetectCycles () && ContainerHelper.isNotEmpty (aObjectsInValue))
      {
        for (final JSONObject aChild : aObjectsInValue)
        {
          aChild.m_aParents.remove (this);
          this.m_aChildren.remove (aChild);
        }
      }
    }
    return EChange.CHANGED;
  }

  @Override
  public EChange apply (@Nullable final IJSONObject aObjectToApply)
  {
    EChange eChange = EChange.UNCHANGED;
    if (aObjectToApply != null)
    {
      for (final String sPropName : aObjectToApply.getAllPropertyNames ())
      {
        final IJSONProperty <?> aProperty = aObjectToApply.getProperty (sPropName, true);
        if (aProperty != null)
        {
          setProperty (sPropName, aObjectToApply.getProperty (sPropName).getValue ());
          eChange = EChange.CHANGED;
        }
      }
    }
    return eChange;
  }

  @Override
  public EChange apply (@Nullable final IJSONObject aObjectToApply, @Nonnull @Nonempty final String sPropertyName)
  {
    EChange eChange = EChange.UNCHANGED;
    if (aObjectToApply != null)
    {
      final IJSONProperty <?> aProperty = aObjectToApply.getProperty (sPropertyName, true);
      if (aProperty != null)
      {
        this.setProperty (aProperty, ECloneStategy.FORCE);
        eChange = EChange.CHANGED;
      }
    }
    return eChange;
  }

  @Override
  public boolean containsNotParsableProperty ()
  {
    for (final IJSONProperty <?> aProperty : this.m_aProperties.values ())
    {
      final IJSONPropertyValue <?> aValue = aProperty.getValue ();
      if (aValue instanceof IJSONPropertyValueNotParsable <?>)
      {
        return true;
      }
      if (aValue instanceof IJSONObject && ((IJSONObject) aValue).containsNotParsableProperty ())
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isEmpty ()
  {
    return this.m_aProperties.isEmpty ();
  }

  @Override
  @Nonnegative
  public int getPropertyCount ()
  {
    return this.m_aProperties.size ();
  }

  @Override
  @Nonnull
  public JSONObject getClone ()
  {
    return new JSONObject (this.m_aProperties.values (), ECloneStategy.FORCE);
  }

  @Override
  public boolean equals (final Object aOther)
  {
    if (aOther == this)
    {
      return true;
    }
    if (!super.equals (aOther))
    {
      return false;
    }
    final JSONObject rhs = (JSONObject) aOther;
    return EqualsUtils.equals (this.m_aProperties, rhs.m_aProperties);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (this.m_aProperties).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("properties", this.m_aProperties).toString (); //$NON-NLS-1$
  }

  @Override
  public IJSONObject getAsJSON ()
  {
    return this;
  }
}
