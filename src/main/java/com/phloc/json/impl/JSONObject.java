/**
 * Copyright (C) 2006-2012 phloc systems
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
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

/**
 * Represents a JSON object having a map of named JSON properties
 * 
 * @author Boris Gregorcic, philip
 */
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
    super (null);
  }

  /**
   * Ctor for using {@link IJSONObject} as {@link IJSONPropertyValue}
   * 
   * @param aValue
   *        a JSON object to store as property
   */
  public JSONObject (@Nullable final IJSONObject aValue)
  {
    super (aValue == null ? null : aValue.getClone ());
  }

  public JSONObject (@Nonnull final Collection <? extends IJSONProperty <?>> aProperties)
  {
    super (null);
    if (aProperties == null)
      throw new NullPointerException ("properties");

    for (final IJSONProperty <?> aProperty : aProperties)
      setProperty (aProperty);
  }

  /**
   * Override since otherwise JSONObjects might return null for certain
   * constructors
   */
  @Override
  @Nonnull
  public IJSONObject getData ()
  {
    final IJSONObject aValue = super.getData ();
    return aValue == null ? this : aValue;
  }

  public void setProperty (@Nonnull final IJSONProperty <?> aProperty)
  {
    if (aProperty == null)
      throw new NullPointerException ("property");

    m_aProperties.put (aProperty.getName (), aProperty.getClone ());
  }

  @Nullable
  public IJSONProperty <?> getProperty (@Nullable final String sName)
  {
    return m_aProperties.get (sName);
  }

  @Nonnull
  @ReturnsImmutableObject
  public Set <String> getAllPropertyNames ()
  {
    return ContainerHelper.makeUnmodifiable (m_aProperties.keySet ());
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
  @edu.umd.cs.findbugs.annotations.SuppressWarnings ("NP_BOOLEAN_RETURN_NULL")
  public Boolean getBooleanProperty (final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBoolean)
      return ((JSONPropertyValueBoolean) aValue).getData ();
    return null;
  }

  public void setBooleanProperty (@Nonnull final String sName, final boolean bDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (bDataValue)));
  }

  public void setBooleanProperty (@Nonnull final String sName, @Nullable final Boolean aDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (aDataValue)));
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

  public void setDoubleProperty (@Nonnull final String sName, final double nDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (nDataValue)));
  }

  public void setDoubleProperty (@Nonnull final String sName, @Nullable final Double aDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (aDataValue)));
  }

  @Nullable
  public Integer getIntegerProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueInteger)
      return ((JSONPropertyValueInteger) aValue).getData ();
    return null;
  }

  public void setIntegerProperty (@Nonnull final String sName, final int nDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (nDataValue)));
  }

  public void setIntegerProperty (@Nonnull final String sName, @Nullable final Integer aDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (aDataValue)));
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

  public void setLongProperty (@Nonnull final String sName, final long nDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueLong (nDataValue)));
  }

  public void setLongProperty (@Nonnull final String sName, @Nullable final Long aDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueLong (aDataValue)));
  }

  @Nullable
  public String getKeywordProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueKeyword)
      return ((JSONPropertyValueKeyword) aValue).getData ();
    return null;
  }

  public void setKeywordProperty (@Nonnull final String sName, @Nullable final String sDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueKeyword (sDataValue)));
  }

  public void setFunctionProperty (@Nonnull final String sName,
                                   @Nonnull final String sBody,
                                   @Nonnull final String... aParams)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueFunction (sBody, aParams)));
  }

  public void setFunctionPrebuildProperty (@Nonnull final String sName, @Nonnull final String sFunctionCode)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueFunctionPrebuild (sFunctionCode)));
  }

  @SuppressWarnings ("unchecked")
  public <I> List <I> getListProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
      return ((IJSONPropertyValueList <I>) aValue).getDataValues ();
    return null;
  }

  public <DATATYPE> void setListProperty (@Nonnull final String sName, final IJSONPropertyValueList <DATATYPE> aList)
  {
    setProperty (JSONProperty.create (sName, aList));
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

  public void setObjectProperty (@Nonnull final String sName, @Nonnull final IJSONObject aObject)
  {
    setProperty (JSONProperty.create (sName, aObject));
  }

  @Nullable
  public String getStringProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueString)
      return ((JSONPropertyValueString) aValue).getData ();
    return null;
  }

  public void setStringProperty (@Nonnull final String sName, final String sDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueString (sDataValue)));
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

  public void setBigIntegerProperty (@Nonnull final String sName, @Nullable final BigInteger aDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueBigInteger (aDataValue)));
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
      return new BigDecimal (((JSONPropertyValueDouble) aValue).getData ().toString ());
    if (aValue instanceof JSONPropertyValueLong)
      return BigDecimal.valueOf (((JSONPropertyValueLong) aValue).getData ().longValue ());
    if (aValue instanceof JSONPropertyValueInteger)
      return BigDecimal.valueOf (((JSONPropertyValueInteger) aValue).getData ().longValue ());
    return null;
  }

  public void setBigDecimalProperty (@Nonnull final String sName, @Nullable final BigDecimal aDataValue)
  {
    setProperty (JSONProperty.create (sName, new JSONPropertyValueBigDecimal (aDataValue)));
  }

  public void setProperty (@Nonnull final String sName, @Nullable final Object aValue)
  {
    // Default: no type converter
    setProperty (sName, aValue, false);
  }

  public void setProperty (@Nonnull final String sName, @Nullable final Object aValue, final boolean bUseTypeConverter)
  {
    if (aValue == null)
      removeProperty (sName);
    else
      if (aValue instanceof IJSONPropertyValue <?>)
        setProperty (JSONProperty.create (sName, (IJSONPropertyValue <?>) aValue));
      else
        if (aValue instanceof IJSONObject)
          setObjectProperty (sName, (IJSONObject) aValue);
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
  }

  @Nullable
  public List <? extends IJSONPropertyValue <?>> getListValues (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
      return ((IJSONPropertyValueList <?>) aValue).getValues ();
    return null;
  }

  public void setObjectListProperty (@Nonnull final String sName, final Collection <? extends IJSONObject> aObjectList)
  {
    final IJSONPropertyValueList <IJSONObject> aList = new JSONPropertyValueList <IJSONObject> ();
    for (final IJSONObject aObject : aObjectList)
      aList.addValue (new JSONPropertyValueJSONObject (aObject));
    setProperty (JSONProperty.create (sName, aList));
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

  public void setStringListProperty (@Nonnull final String sName, @Nonnull final Collection <String> aStringList)
  {
    final IJSONPropertyValueList <String> aList = new JSONPropertyValueList <String> ();
    for (final String sValue : aStringList)
      aList.addValue (new JSONPropertyValueString (sValue));
    setProperty (JSONProperty.create (sName, aList));
  }

  public void setIntegerListProperty (@Nonnull final String sName, @Nonnull final int [] aIntList)
  {
    final IJSONPropertyValueList <Integer> aList = new JSONPropertyValueList <Integer> ();
    for (final int nValue : aIntList)
      aList.addValue (new JSONPropertyValueInteger (nValue));
    setProperty (JSONProperty.create (sName, aList));
  }

  public void setListOfListProperty (@Nonnull final String sName,
                                     @Nonnull final Collection <Collection <String>> aListOfList)
  {
    final JSONPropertyValueList <List <IJSONPropertyValue <String>>> aList = new JSONPropertyValueList <List <IJSONPropertyValue <String>>> ();
    for (final Collection <String> aRow : aListOfList)
    {
      final IJSONPropertyValueList <String> aRowList = new JSONPropertyValueList <String> ();
      for (final String aCell : aRow)
        aRowList.addValue (new JSONPropertyValueString (aCell));
      aList.addValue (aRowList);
    }
    setProperty (JSONProperty.create (sName, aList));
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JSONObject}
   * 
   * @param aNode
   * @return the resulting object
   * @throws JSONParsingException
   */
  @Nonnull
  public static JSONObject fromJSONNode (@Nonnull final JsonNode aNode) throws JSONParsingException
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    final JSONObject aObj = new JSONObject ();
    final Iterator <Map.Entry <String, JsonNode>> aFieldIterator = aNode.fields ();
    while (aFieldIterator.hasNext ())
    {
      final Map.Entry <String, JsonNode> aEntry = aFieldIterator.next ();
      final String sField = aEntry.getKey ();
      final JsonNode aValue = aEntry.getValue ();

      if (aValue.isObject ())
        aObj.setObjectProperty (sField, JSONObject.fromJSONNode (aValue));
      else
        if (aValue.isArray ())
          aObj.setListProperty (sField, JSONPropertyValueList.fromJSONNode ((ArrayNode) aValue));
        else
          if (aValue.isBoolean ())
            aObj.setBooleanProperty (sField, aValue.booleanValue ());
          else
            if (aValue.isTextual ())
              aObj.setStringProperty (sField, aValue.textValue ());
            else
              if (aValue.isBigDecimal ())
                aObj.setBigDecimalProperty (sField, aValue.decimalValue ());
              else
                if (aValue.isFloatingPointNumber ())
                  aObj.setDoubleProperty (sField, aValue.doubleValue ());
                else
                  if (aValue.isBigInteger ())
                    aObj.setBigIntegerProperty (sField, aValue.bigIntegerValue ());
                  else
                    if (aValue.isLong ())
                      aObj.setLongProperty (sField, aValue.longValue ());
                    else
                      if (aValue.isInt ())
                        aObj.setIntegerProperty (sField, aValue.intValue ());
                      else
                        if (aValue.isNull ())
                        {
                          // just do not set null values
                        }
                        else
                          throw new JSONParsingException ("Unhandled value type: " + aValue);
    }
    return aObj;
  }

  @Override
  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
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
  public EChange removeProperty (final String sName)
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
    return EqualsUtils.nullSafeEquals (m_aProperties, rhs.m_aProperties);
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
