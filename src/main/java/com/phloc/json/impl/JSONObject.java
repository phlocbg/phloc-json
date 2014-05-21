/**
 * Copyright (C) 2006-2014 phloc systems
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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.IJSONPropertyValueNotParsable;
import com.phloc.json.impl.value.AbstractJSONPropertyValue;
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
  private static final Logger s_aLogger = LoggerFactory.getLogger (JSONObject.class);

  private final Map <String, IJSONProperty <?>> m_aProperties = new LinkedHashMap <String, IJSONProperty <?>> ();

  /**
   * Default Ctor. Handle with care as it by default sets a <code>null</code>
   * value which is most probable be crashing somewhere inside this class, if no
   * data is provided afterwards!
   */
  public JSONObject ()
  {
    super ();
  }

  public JSONObject (@Nonnull final Iterable <? extends IJSONProperty <?>> aProperties)
  {
    super ();
    if (aProperties == null)
      throw new NullPointerException ("properties");

    for (final IJSONProperty <?> aProperty : aProperties)
      setProperty (aProperty);
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

  @Nonnull
  public JSONObject setProperty (@Nonnull final IJSONProperty <?> aProperty)
  {
    if (aProperty == null)
      throw new NullPointerException ("property");

    m_aProperties.put (aProperty.getName (), aProperty.getClone ());
    return this;
  }

  @Nullable
  public IJSONProperty <?> getProperty (@Nullable final String sName)
  {
    return m_aProperties.get (sName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllPropertyNames ()
  {
    return ContainerHelper.newOrderedSet (m_aProperties.keySet ());
  }

  @Nullable
  private IJSONPropertyValue <?> _getPropertyValueInternal (@Nullable final String sName)
  {
    final IJSONProperty <?> aProperty = m_aProperties.get (sName);
    return aProperty == null ? null : aProperty.getValue ();
  }

  // we need to differentiate here the case of set boolean values from the case
  // the property is not found or of wrong type (null). Un-boxing is prevented
  // by PMD anyway and the method is annotated as @Nullable
  @Nullable
  @SuppressFBWarnings ("NP_BOOLEAN_RETURN_NULL")
  public Boolean getBooleanProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBoolean)
      return ((JSONPropertyValueBoolean) aValue).getData ();
    return null;
  }

  @Nonnull
  public JSONObject setBooleanProperty (@Nonnull final String sName, final boolean bDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (bDataValue)));
  }

  @Nonnull
  public JSONObject setBooleanProperty (@Nonnull final String sName, @Nonnull final Boolean aDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (aDataValue)));
  }

  @Nullable
  public Double getDoubleProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueDouble)
      return ((JSONPropertyValueDouble) aValue).getData ();
    // we can also return an integer property as a double (if we use double as a
    // type, still on parsing Jackson might decide that the type is integer for
    // e.g. '1')
    if (aValue instanceof JSONPropertyValueLong)
      return Double.valueOf (((JSONPropertyValueLong) aValue).getData ().doubleValue ());
    if (aValue instanceof JSONPropertyValueInteger)
      return Double.valueOf (((JSONPropertyValueInteger) aValue).getData ().doubleValue ());
    return null;
  }

  @Nonnull
  public JSONObject setDoubleProperty (@Nonnull final String sName, final double nDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (nDataValue)));
  }

  @Nonnull
  public JSONObject setDoubleProperty (@Nonnull final String sName, @Nonnull final Double aDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (aDataValue)));
  }

  @Nullable
  public Integer getIntegerProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueInteger)
      return ((JSONPropertyValueInteger) aValue).getData ();
    return null;
  }

  @Nonnull
  public JSONObject setIntegerProperty (@Nonnull final String sName, final int nDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (nDataValue)));
  }

  @Nonnull
  public JSONObject setIntegerProperty (@Nonnull final String sName, @Nonnull final Integer aDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (aDataValue)));
  }

  @Nullable
  public Long getLongProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueLong)
      return ((JSONPropertyValueLong) aValue).getData ();
    // we can also return an integer property as a long (if we use long as a
    // type, still on parsing Jackson might decide that the type is integer for
    // e.g. '1')
    if (aValue instanceof JSONPropertyValueInteger)
      return Long.valueOf (((JSONPropertyValueInteger) aValue).getData ().longValue ());
    return null;
  }

  @Nonnull
  public JSONObject setLongProperty (@Nonnull final String sName, final long nDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueLong (nDataValue)));
  }

  @Nonnull
  public JSONObject setLongProperty (@Nonnull final String sName, @Nonnull final Long aDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueLong (aDataValue)));
  }

  @Nullable
  public String getKeywordProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueKeyword)
      return ((JSONPropertyValueKeyword) aValue).getData ();
    return null;
  }

  @Nonnull
  public JSONObject setKeywordProperty (@Nonnull final String sName, @Nonnull final String sDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueKeyword (sDataValue)));
  }

  @Nonnull
  public JSONObject setFunctionProperty (@Nonnull final String sName,
                                         @Nonnull final String sBody,
                                         @Nullable final String... aParams)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueFunction (sBody, aParams)));
  }

  @Nonnull
  public JSONObject setFunctionPrebuildProperty (@Nonnull final String sName, @Nonnull final String sFunctionCode)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueFunctionPrebuild (sFunctionCode)));
  }

  @SuppressWarnings ("unchecked")
  public <I> List <I> getListProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
      return (List <I>) ((IJSONPropertyValueList <?>) aValue).getDataValues ();
    return null;
  }

  @Nonnull
  public JSONObject setListProperty (@Nonnull final String sName, @Nonnull final IJSONPropertyValueList <?> aList)
  {
    return setProperty (JSONProperty.create (sName, aList));
  }

  @Nullable
  public IJSONObject getObjectProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof IJSONObject)
      return (IJSONObject) aValue;
    if (aValue instanceof JSONPropertyValueJSONObject)
      return ((JSONPropertyValueJSONObject) aValue).getData ();
    return null;
  }

  @Nonnull
  public JSONObject setObjectProperty (@Nonnull final String sName, @Nonnull final IJSONObject aObject)
  {
    return setProperty (JSONProperty.create (sName, aObject));
  }

  @Nullable
  public String getStringProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueString)
      return ((JSONPropertyValueString) aValue).getData ();
    return null;
  }

  @Nonnull
  public JSONObject setStringProperty (@Nonnull final String sName, @Nonnull final String sDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueString (sDataValue)));
  }

  @Nonnull
  public IJSONObject setStringProperties (@Nonnull final Map <String, String> aMap)
  {
    for (final Map.Entry <String, String> aEntry : aMap.entrySet ())
      setStringProperty (aEntry.getKey (), aEntry.getValue ());
    return this;
  }

  @Nullable
  public BigInteger getBigIntegerProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBigInteger)
      return ((JSONPropertyValueBigInteger) aValue).getData ();
    // we can also return an integer property as a BigInteger (if we use
    // BigInteger as a type, still on parsing Jackson might decide that the type
    // is integer for e.g. '1')
    if (aValue instanceof JSONPropertyValueLong)
      return BigInteger.valueOf (((JSONPropertyValueLong) aValue).getData ().longValue ());
    if (aValue instanceof JSONPropertyValueInteger)
      return BigInteger.valueOf (((JSONPropertyValueInteger) aValue).getData ().longValue ());
    return null;
  }

  @Nonnull
  public JSONObject setBigIntegerProperty (@Nonnull final String sName, @Nonnull final BigInteger aDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBigInteger (aDataValue)));
  }

  @Nullable
  public BigDecimal getBigDecimalProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBigDecimal)
      return ((JSONPropertyValueBigDecimal) aValue).getData ();
    // we can also return an integer property as a BigDecimal (if we use
    // BigDecimal as a type, still on parsing Jackson might decide that the type
    // is integer for e.g. '1')
    if (aValue instanceof JSONPropertyValueBigInteger)
      return new BigDecimal (((JSONPropertyValueBigInteger) aValue).getData ());
    if (aValue instanceof JSONPropertyValueDouble)
      return StringParser.parseBigDecimal (((JSONPropertyValueDouble) aValue).getData ().toString ());
    if (aValue instanceof JSONPropertyValueLong)
      return BigDecimal.valueOf (((JSONPropertyValueLong) aValue).getData ().longValue ());
    if (aValue instanceof JSONPropertyValueInteger)
      return BigDecimal.valueOf (((JSONPropertyValueInteger) aValue).getData ().longValue ());
    return null;
  }

  @Nonnull
  public JSONObject setBigDecimalProperty (@Nonnull final String sName, @Nonnull final BigDecimal aDataValue)
  {
    return setProperty (JSONProperty.create (sName, new JSONPropertyValueBigDecimal (aDataValue)));
  }

  @Nonnull
  public JSONObject setProperty (@Nonnull final String sName, @Nullable final Object aValue)
  {
    // Default: no type converter
    return setProperty (sName, aValue, false);
  }

  @Nonnull
  public JSONObject setProperty (@Nonnull final String sName,
                                 @Nullable final Object aValue,
                                 final boolean bUseTypeConverter)
  {
    if (aValue == null)
      removeProperty (sName);
    else
      if (aValue instanceof IJSONObject)
        setObjectProperty (sName, (IJSONObject) aValue);
      else
        if (aValue instanceof IJSONPropertyValue <?>)
          setProperty (JSONProperty.create (sName, (IJSONPropertyValue <?>) aValue));
        else
          if (aValue instanceof Boolean)
            setBooleanProperty (sName, (Boolean) aValue);
          else
            if (aValue instanceof BigInteger)
              setBigIntegerProperty (sName, (BigInteger) aValue);
            else
              if (aValue instanceof BigDecimal)
                setBigDecimalProperty (sName, (BigDecimal) aValue);
              else
                if (aValue instanceof Double)
                  setDoubleProperty (sName, (Double) aValue);
                else
                  if (aValue instanceof Integer)
                    setIntegerProperty (sName, (Integer) aValue);
                  else
                    if (aValue instanceof Long)
                      setLongProperty (sName, (Long) aValue);
                    else
                      if (aValue instanceof String)
                        setStringProperty (sName, (String) aValue);
                      else
                      {
                        // Unknown type -> use type converter?
                        String sValue;
                        if (bUseTypeConverter)
                        {
                          sValue = TypeConverter.convertIfNecessary (aValue, String.class);
                        }
                        else
                        {
                          s_aLogger.warn ("Setting property of type " +
                                          aValue.getClass ().getName () +
                                          " as String without TypeConverter!");
                          sValue = String.valueOf (aValue);
                        }
                        setStringProperty (sName, sValue);
                      }
    return this;
  }

  @Nullable
  public List <? extends IJSONPropertyValue <?>> getListValues (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
      return ((IJSONPropertyValueList <?>) aValue).getValues ();
    return null;
  }

  @Nonnull
  public JSONObject setObjectListProperty (@Nonnull final String sName,
                                           @Nonnull final Iterable <? extends IJSONObject> aObjectList)
  {
    final IJSONPropertyValueList <IJSONObject> aList = new JSONPropertyValueList <IJSONObject> ();
    for (final IJSONObject aObject : aObjectList)
      aList.addValue (aObject);
    return setProperty (JSONProperty.create (sName, aList));
  }

  @Nonnull
  public List <IJSONObject> getObjectListProperty (@Nullable final String sName)
  {
    final List <IJSONObject> aReturn = new ArrayList <IJSONObject> ();
    final List <?> aList = getListProperty (sName);
    if (aList != null)
      for (final Object aValue : aList)
        if (aValue instanceof IJSONObject)
          aReturn.add ((IJSONObject) aValue);
    return aReturn;
  }

  @Nonnull
  public JSONObject setStringListProperty (@Nonnull final String sName, @Nonnull final Iterable <String> aStringList)
  {
    final IJSONPropertyValueList <JSONPropertyValueString> aList = new JSONPropertyValueList <JSONPropertyValueString> ();
    for (final String sValue : aStringList)
      aList.addValue (new JSONPropertyValueString (sValue));
    return setProperty (JSONProperty.create (sName, aList));
  }

  @Nonnull
  public JSONObject setIntegerListProperty (@Nonnull final String sName, @Nonnull final int [] aIntList)
  {
    final IJSONPropertyValueList <JSONPropertyValueInteger> aList = new JSONPropertyValueList <JSONPropertyValueInteger> ();
    for (final int nValue : aIntList)
      aList.addValue (new JSONPropertyValueInteger (nValue));
    return setProperty (JSONProperty.create (sName, aList));
  }

  @Deprecated
  @Nonnull
  public JSONObject setListOfListProperty (@Nonnull final String sName,
                                           @Nonnull final Iterable <? extends Iterable <String>> aListOfList)
  {
    return setListOfStringListProperty (sName, aListOfList);
  }

  @Nonnull
  public JSONObject setListOfStringListProperty (@Nonnull final String sName,
                                                 @Nonnull final Iterable <? extends Iterable <String>> aListOfList)
  {
    final JSONPropertyValueList <IJSONPropertyValueList <JSONPropertyValueString>> aList = new JSONPropertyValueList <IJSONPropertyValueList <JSONPropertyValueString>> ();
    for (final Iterable <String> aRow : aListOfList)
    {
      final IJSONPropertyValueList <JSONPropertyValueString> aRowList = new JSONPropertyValueList <JSONPropertyValueString> ();
      for (final String aCell : aRow)
        aRowList.addValue (new JSONPropertyValueString (aCell));
      aList.addValue (aRowList);
    }
    return setProperty (JSONProperty.create (sName, aList));
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
      final IJSONProperty <?> aProperty = getProperty (sProperty);
      aProperty.appendJSONString (aResult, bAlignAndIndent, nLevel + 1);
      if (nIndex < aPropertyNames.size () - 1)
        aResult.append (CJSONConstants.TOKEN_SEPARATOR);
      appendNewLine (aResult, bAlignAndIndent);
      nIndex++;
    }
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.OBJECT_END);
  }

  @Nonnull
  public EChange removeProperty (@Nullable final String sName)
  {
    return EChange.valueOf (m_aProperties.remove (sName) != null);
  }

  public boolean containsNotParsableProperty ()
  {
    for (final IJSONProperty <?> aProperty : m_aProperties.values ())
    {
      final IJSONPropertyValue <?> aValue = aProperty.getValue ();
      if (aValue instanceof IJSONPropertyValueNotParsable <?>)
        return true;
      if (aValue instanceof IJSONObject && ((IJSONObject) aValue).containsNotParsableProperty ())
        return true;
    }
    return false;
  }

  public boolean isEmpty ()
  {
    return m_aProperties.isEmpty ();
  }

  @Nonnegative
  public int getPropertyCount ()
  {
    return m_aProperties.size ();
  }

  @Nonnull
  public JSONObject getClone ()
  {
    return new JSONObject (m_aProperties.values ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final JSONObject rhs = (JSONObject) o;
    return EqualsUtils.equals (m_aProperties, rhs.m_aProperties);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aProperties).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("properties", m_aProperties).toString ();
  }
}
