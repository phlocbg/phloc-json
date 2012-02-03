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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.impl.value.AbstractJSONPropertyValue;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBigInteger;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueDouble;
import com.phloc.json.impl.value.JSONPropertyValueFunction;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueJSONObject;
import com.phloc.json.impl.value.JSONPropertyValueKeyword;
import com.phloc.json.impl.value.JSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueLong;
import com.phloc.json.impl.value.JSONPropertyValueString;

/**
 * @author Boris Gregorcic
 */
public class JSONObject extends AbstractJSONPropertyValue <IJSONObject> implements IJSONObject
{
  private final Map <String, IJSONProperty <?>> m_aProperties = new LinkedHashMap <String, IJSONProperty <?>> ();

  /**
   * Default Ctor
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
    super (aValue);
  }

  public JSONObject (@Nonnull final Collection <IJSONProperty <?>> aProps)
  {
    super (null);
    for (final IJSONProperty <?> aProp : aProps)
      addProperty (aProp);
  }

  public void addProperty (@Nonnull final IJSONProperty <?> aProperty)
  {
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
    addProperty (JSONProperty.create (sName, new JSONPropertyValueBoolean (bDataValue)));
  }

  @Nullable
  public Double getDoubleProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueDouble)
      return ((JSONPropertyValueDouble) aValue).getData ();
    return null;
  }

  public void setDoubleProperty (@Nonnull final String sName, final double nDataValue)
  {
    addProperty (JSONProperty.create (sName, new JSONPropertyValueDouble (nDataValue)));
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
    addProperty (JSONProperty.create (sName, new JSONPropertyValueInteger (nDataValue)));
  }

  @Nullable
  public Long getLongProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueLong)
      return ((JSONPropertyValueLong) aValue).getData ();
    return null;
  }

  public void setLongProperty (@Nonnull final String sName, final long nDataValue)
  {
    addProperty (JSONProperty.create (sName, new JSONPropertyValueLong (nDataValue)));
  }

  @Nullable
  public String getKeywordProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueKeyword)
      return ((JSONPropertyValueKeyword) aValue).getData ();
    return null;
  }

  public void setKeywordProperty (@Nonnull final String sName, final String sDataValue)
  {
    addProperty (JSONProperty.create (sName, new JSONPropertyValueKeyword (sDataValue)));
  }

  public void setFunctionProperty (@Nonnull final String sName, final String sBody, final String... aParams)
  {
    addProperty (JSONProperty.create (sName, new JSONPropertyValueFunction (sBody, aParams)));
  }

  public List <?> getListProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof IJSONPropertyValueList <?>)
      return ((IJSONPropertyValueList <?>) aValue).getDataValues ();
    return null;
  }

  public <DATATYPE> void setListProperty (@Nonnull final String sName, final IJSONPropertyValueList <DATATYPE> aList)
  {
    addProperty (JSONProperty.create (sName, aList));
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

  public void setObjectProperty (@Nonnull final String sName, final IJSONObject aObject)
  {
    addProperty (JSONProperty.create (sName, aObject));
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
    addProperty (JSONProperty.create (sName, new JSONPropertyValueString (sDataValue)));
  }

  @Nullable
  public BigInteger getBigIntegerProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBigInteger)
      return ((JSONPropertyValueBigInteger) aValue).getData ();
    return null;
  }

  public void setBigIntegerProperty (@Nonnull final String sName, final BigInteger aDataValue)
  {
    addProperty (JSONProperty.create (sName, new JSONPropertyValueBigInteger (aDataValue)));
  }

  @Nullable
  public BigDecimal getBigDecimalProperty (@Nullable final String sName)
  {
    final IJSONPropertyValue <?> aValue = _getPropertyValueInternal (sName);
    if (aValue instanceof JSONPropertyValueBigDecimal)
      return ((JSONPropertyValueBigDecimal) aValue).getData ();
    return null;
  }

  public void setBigDecimalProperty (@Nonnull final String sName, final BigDecimal aDataValue)
  {
    addProperty (JSONProperty.create (sName, new JSONPropertyValueBigDecimal (aDataValue)));
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
    addProperty (JSONProperty.create (sName, aList));
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
    addProperty (JSONProperty.create (sName, aList));
  }

  public void setIntegerListProperty (@Nonnull final String sName, @Nonnull final int [] aIntList)
  {
    final IJSONPropertyValueList <Integer> aList = new JSONPropertyValueList <Integer> ();
    for (final int nValue : aIntList)
      aList.addValue (new JSONPropertyValueInteger (nValue));
    addProperty (JSONProperty.create (sName, aList));
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
    addProperty (JSONProperty.create (sName, aList));
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
    final Iterator <Map.Entry <String, JsonNode>> aFieldIterator = aNode.getFields ();
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
            aObj.setBooleanProperty (sField, aValue.getBooleanValue ());
          else
            if (aValue.isTextual ())
              aObj.setStringProperty (sField, aValue.getTextValue ());
            else
              if (aValue.isBigDecimal ())
                aObj.setBigDecimalProperty (sField, aValue.getDecimalValue ());
              else
                if (aValue.isFloatingPointNumber ())
                  aObj.setDoubleProperty (sField, aValue.getDoubleValue ());
                else
                  if (aValue.isBigInteger ())
                    aObj.setBigIntegerProperty (sField, aValue.getBigIntegerValue ());
                  else
                    if (aValue.isLong ())
                      aObj.setLongProperty (sField, aValue.getLongValue ());
                    else
                      if (aValue.isInt ())
                        aObj.setIntegerProperty (sField, aValue.getIntValue ());
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
