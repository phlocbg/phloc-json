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
package com.phloc.json;// NOPMD

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.state.EChange;
import com.phloc.json.impl.JSONObject;
import com.phloc.json.impl.JSONReader;

/**
 * A basic JSON object
 *
 * @author Boris Gregorcic
 * @author Philip Helger
 */
public interface IJSONObject extends IJSONPropertyValueComplex <IJSONObject>
{
  /**
   * adds the passed property
   *
   * @param aProperty
   *        The property to be set
   * @return this
   */
  @Nonnull
  IJSONObject setProperty (@Nonnull IJSONProperty <?> aProperty);

  /**
   * Get the property with the specified name
   *
   * @param sName
   *        name of the requested property
   * @return the corresponding {@link IJSONProperty} if found, <code>null</code>
   *         otherwise
   */
  @Nullable
  IJSONProperty <?> getProperty (@Nullable String sName);

  /**
   * Get the data value of the specified property directly
   *
   * @param sName
   *        the property name
   * @return the direct data value or <code>null</code>
   */
  Object getPropertyValueData (@Nullable String sName);

  /**
   * Get the numeric property with the specified name<br>
   * <b>Numeric properties are:</b>
   * <ul>
   * <li><code>Double</code></li>
   * <li><code>Integer</code></li>
   * <li><code>Long</code></li>
   * <li><code>BigDecimal</code></li>
   * <li><code>BigInteger</code></li>
   * </ul>
   *
   * @param sName
   *        name of the requested property
   * @return the corresponding value of the property if found, <code>null</code>
   *         otherwise
   */
  @Nullable
  Object getNumericProperty (@Nullable String sName);

  /**
   * Get a list of all property names of this object
   *
   * @return the property names (according to property ordering)
   */
  @Nonnull
  Set <String> getAllPropertyNames ();

  /**
   * Tries to resolve the value of the passed boolean property. If not found or
   * the property with the passed name is not of type <code>Boolean</code>,
   * <code>null</code> will be returned.
   *
   * @param sName
   *        Property name
   * @return the Boolean value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  Boolean getBooleanProperty (@Nullable String sName);

  /**
   * Tries to resolve the boolean property with the passed name. If such a
   * property cannot be found, the passed default value is returned.
   * 
   * @param sName
   * @param bDefault
   * @return The simple boolean value of the requested property, or the passed
   *         default value as fall-back
   */
  boolean getBoolProperty (@Nullable String sName, boolean bDefault);

  /**
   * An easy way to set a new boolean property in a JSON object. Existing
   * properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param bDataValue
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setBooleanProperty (@Nonnull String sName, boolean bDataValue);

  @Nonnull
  IJSONObject setBooleanProperty (@Nonnull String sName, @Nonnull Boolean aDataValue);

  /**
   * Tries to resolve the value of the passed double property. If not found or
   * the property with the passed name is not of a compatible type,
   * <code>null</code> will be returned.<br>
   * <b>Compatible types:</b>
   * <ul>
   * <li><code>Double</code></li>
   * <li><code>Integer</code></li>
   * <li><code>Long</code></li>
   * <li><code>BigDecimal</code></li>
   * <li><code>BigInteger</code></li>
   * </ul>
   *
   * @param sName
   *        Property name
   * @return the Double value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  Double getDoubleProperty (@Nullable String sName);

  /**
   * An easy way to set a new double property in a JSON object. Existing
   * properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param dDataValue
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setDoubleProperty (@Nonnull String sName, double dDataValue);

  @Nonnull
  IJSONObject setDoubleProperty (@Nonnull String sName, @Nonnull Double aDataValue);

  /**
   * Tries to resolve the value of the passed integer property. If not found or
   * the property with the passed name is not of a compatible type,
   * <code>null</code> will be returned.<br>
   * <b>Compatible types:</b>
   * <ul>
   * <li><code>Integer</code></li>
   * <li><code>Double</code></li>
   * <li><code>Long</code></li>
   * <li><code>BigDecimal</code></li>
   * <li><code>BigInteger</code></li>
   * </ul>
   *
   * @param sName
   *        Property name
   * @return the Integer value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  Integer getIntegerProperty (@Nullable String sName);

  /**
   * Tries to resolve the value using {@link #getIntegerProperty(String)}. If no
   * valid value is available, this method will throw a
   * {@link NullPointerException} rather than returning null.
   *
   * @param sName
   *        Property name
   * @return the Integer value of the property with the passed name, never
   *         <code>null</code>
   */
  @Nonnull
  Integer getIntegerPropertyNonNull (@Nullable String sName);

  /**
   * Tries to resolve the integer property with the passed name. If such a
   * property cannot be found, the passed default value is returned.
   *
   * @param sName
   * @param nDefault
   * @return The simple integer value of the requested property, or the passed
   *         default value as fall-back
   */
  int getIntProperty (@Nullable String sName, int nDefault);

  /**
   * An easy way to set a new int property in a JSON object. Existing properties
   * with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param nDataValue
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setIntegerProperty (@Nonnull String sName, int nDataValue);

  @Nonnull
  IJSONObject setIntegerProperty (@Nonnull String sName, @Nonnull Integer aDataValue);

  /**
   * Tries to resolve the value of the passed long property. If not found or the
   * property with the passed name is not of type <code>Long</code>,
   * <code>null</code> will be returned.
   *
   * @param sName
   *        Property name
   * @return the Long value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  Long getLongProperty (@Nullable String sName);

  /**
   * An easy way to set a new long property in a JSON object. Existing
   * properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param nDataValue
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setLongProperty (@Nonnull String sName, long nDataValue);

  @Nonnull
  IJSONObject setLongProperty (@Nonnull String sName, @Nonnull Long aDataValue);

  /**
   * Tries to resolve the value of the passed keyword property. If not found or
   * the property with the passed name is not of type <code>Keyword</code>,
   * <code>null</code> will be returned.
   *
   * @param sName
   *        Property name
   * @return the String value of the keyword valuer of the property with the
   *         passed name, or <code>null</code>
   */
  @Nullable
  String getKeywordProperty (@Nullable String sName);

  /**
   * An easy way to set a new keyword property (not quoted) in a JSON object.
   * Existing properties with the same name will be replaced!<br>
   * <b>ATTENTION:</b> keyword properties cannot be parsed again due to the
   * fact, that the {@link JSONReader#parseObject(String)} uses the default
   * {@link com.fasterxml.jackson.databind.ObjectMapper} which is not capable to
   * handle keywords.
   *
   * @param sName
   *        Property name
   * @param sDataValue
   *        The property value. Will be JSON escaped.
   * @return this
   */
  @Nonnull
  IJSONObject setKeywordProperty (@Nonnull String sName, @Nonnull String sDataValue);

  /**
   * An easy way to set a new function property (not JSON escaped) in a JSON
   * object. The function keyword and the parenthesis for parameters as well as
   * the body braces will be automatically created.<br>
   * <b>ATTENTION:</b> function properties cannot be parsed again due to the
   * fact, that the {@link JSONReader#parseObject(String)} uses the default
   * {@link com.fasterxml.jackson.databind.ObjectMapper} which is not capable to
   * handle functions.
   *
   * @param sName
   *        The name of the function property
   * @param sBody
   *        The function body JS code
   * @param aParams
   *        The function parameter names
   * @return this
   * @deprecated Will be removed as functions are not allowed in JSON and cannot
   *             be parsed by any JSON parsers (also not on the client side).
   *             Therefore, there is no point to support this. If you need to
   *             transport functions, wrap them in a string and evaluate them on
   *             the client side.
   */
  @Nonnull
  @Deprecated
  IJSONObject setFunctionProperty (@Nonnull String sName, @Nonnull String sBody, @Nullable String... aParams);

  /**
   * An easy way to set a new function property (not JSON escaped) in a JSON
   * object. The function code must be completely ready starting with the
   * "function" keyword and with the body! The code must already be JavaScript
   * escaped, as strings inside the function body will not be escaped
   * again!!!!!!!<br>
   * <b>ATTENTION:</b> function properties cannot be parsed again due to the
   * fact, that the {@link JSONReader#parseObject(String)} uses the default
   * {@link com.fasterxml.jackson.databind.ObjectMapper} which is not capable to
   * handle functions.
   *
   * @param sName
   *        The name of the function property
   * @param sFunctionCode
   *        The function JS code
   * @return this
   * @deprecated Will be removed as functions are not allowed in JSON and cannot
   *             be parsed by any JSON parsers (also not on the client side).
   *             Therefore, there is no point to support this. If you need to
   *             transport functions, wrap them in a string and evaluate them on
   *             the client side.
   */
  @Nonnull
  @Deprecated
  IJSONObject setFunctionPrebuildProperty (@Nonnull String sName, @Nonnull String sFunctionCode);

  /**
   * Tries to resolve the value of the passed list property. If not found or the
   * property with the passed name is not of type
   * <code>IJSONPropertyValueList &lt;?&gt;</code>, <code>null</code> will
   * returned.
   *
   * @param sName
   *        Property name
   * @param <I>
   *        Type of the list items
   * @return a list of the inner values stored in the list value of the property
   *         with the passed name, or <code>null</code>
   */
  @Nullable
  <I> List <I> getListProperty (@Nullable String sName);

  /**
   * An easy way to set a new list type property in a JSON object. Existing
   * properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aList
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setListProperty (@Nonnull String sName, @Nonnull IJSONPropertyValueList <?> aList);

  /**
   * Tries to resolve the value of the passed object property. If not found or
   * the property with the passed name is not of type <code>IJSONObject</code>,
   * <code>null</code> will be returned.
   *
   * @param sName
   *        Property name
   * @return the IJSONObject value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  IJSONObject getObjectProperty (@Nullable String sName);

  /**
   * An easy way to set a new JSON object property in a JSON object. Existing
   * properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aObject
   *        the nested object
   * @return this
   */
  @Nonnull
  IJSONObject setObjectProperty (@Nonnull String sName, @Nonnull IJSONObject aObject);

  /**
   * Tries to resolve the value of the passed string property. If not found or
   * the property with the passed name is not of type <code>String</code>,
   * <code>null</code> will be returned.
   *
   * @param sName
   *        Property name
   * @return the String value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  String getStringProperty (@Nullable String sName);

  /**
   * Tries to resolve the value of the passed string property. If not found or
   * the property with the passed name is not of type <code>String</code> an
   * {@link IllegalArgumentException} will be thrown.
   *
   * @param sName
   *        Property name
   * @return the String value of the property with the passed name, never
   *         <code>null</code>
   */
  @Nonnull
  String getStringPropertyNonEmpty (@Nonnull @Nonempty String sName);

  /**
   * An easy way to set a new string property in a JSON object. Existing
   * properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param sDataValue
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setStringProperty (@Nonnull String sName, @Nonnull String sDataValue);

  @Nonnull
  IJSONObject setStringProperties (@Nonnull Map <String, String> aMap);

  @Nullable
  BigInteger getBigIntegerProperty (@Nullable String sName);

  @Nonnull
  IJSONObject setBigIntegerProperty (@Nonnull String sName, @Nonnull BigInteger aDataValue);

  @Nullable
  BigDecimal getBigDecimalProperty (@Nullable String sName);

  @Nonnull
  IJSONObject setBigDecimalProperty (@Nonnull String sName, @Nonnull BigDecimal aDataValue);

  /**
   * Tries to resolve the value of the passed list property. If not found or the
   * property with the passed name is not of type
   * <code>IJSONPropertyValueList &lt;?&gt;</code>, <code>null</code> will
   * returned.
   *
   * @param sName
   *        Property name
   * @return a list of {@link IJSONPropertyValue}s contained, or
   *         <code>null</code>
   */
  @Nullable
  List <? extends IJSONPropertyValue <?>> getListValues (@Nullable String sName);

  @Nonnull
  List <IJSONObject> getObjectListProperty (@Nullable String sName);

  /**
   * An easy way to set a new JSON object list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aObjectList
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setObjectListProperty (@Nonnull String sName, @Nonnull Iterable <? extends IJSONObject> aObjectList);

  /**
   * An easy way to set a new nested list (array of string arrays) property in a
   * JSON object. Existing properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aListOfList
   *        Property value
   * @return this
   * @deprecated Use {@link #setListOfStringListProperty(String,Iterable)}
   *             instead
   */
  @Deprecated
  @Nonnull
  IJSONObject setListOfListProperty (@Nonnull String sName,
                                     @Nonnull Iterable <? extends Iterable <String>> aListOfList);

  /**
   * An easy way to set a new nested list (array of string arrays) property in a
   * JSON object. Existing properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aListOfList
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setListOfStringListProperty (@Nonnull String sName,
                                           @Nonnull Iterable <? extends Iterable <String>> aListOfList);

  /**
   * An easy way to set a new string list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aStringList
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setStringListProperty (@Nonnull String sName, @Nonnull Iterable <String> aStringList);

  /**
   * Creates a list property containing the passed values. Each passed value
   * will be converted to the corresponding JSON value and added if possible.
   * <b>ATTENTION:</b> Only the following types will be converted:
   * <ul>
   * <li>IJSONPropertyValue</li>
   * <li>String</li>
   * <li>Integer</li>
   * <li>Boolean</li>
   * <li>BigInteger</li>
   * <li>BigDecimal</li>
   * <li>Double</li>
   * <li>Long</li>
   * </ul>
   *
   * @param sName
   *        The name of the property
   * @param aValues
   *        The values to set, must not be <code>null</code>, does not allow
   *        <code>null</code> values!
   * @return this
   */
  JSONObject setMixedListProperty (@Nonnull String sName, @Nonnull Iterable <?> aValues);

  /**
   * An easy way to set a new integer list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aIntList
   *        Property value
   * @return this
   */
  @Nonnull
  IJSONObject setIntegerListProperty (@Nonnull String sName, @Nonnull int [] aIntList);

  /**
   * An easy way to set a new integer list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   *
   * @param sName
   *        Property name
   * @param aIntegerList
   *        Property value
   * @return this
   */
  JSONObject setIntegerListProperty (@Nonnull String sName, @Nonnull List <Integer> aIntegerList);

  /**
   * This is a helper method to set an arbitrary property not exactly knowing
   * the internal data type. As a fall-back a string property will be used if no
   * matching data type can be found. No type conversion happens by default.
   *
   * @param sName
   *        Property name
   * @param aValue
   *        value to be set. <code>null</code> means remove the property
   * @return this
   */
  @Nonnull
  IJSONObject setProperty (@Nonnull String sName, @Nullable Object aValue);

  /**
   * This is a helper method to set an arbitrary property not exactly knowing
   * the internal data type. As a fall-back a the type converter to string will
   * be used if the parameter is <code>true</code>.
   *
   * @param sName
   *        Property name
   * @param aValue
   *        value to be set. <code>null</code> means remove the property.
   * @param bUseTypeConverter
   *        if <code>true</code> the type converter to {@link String} will be
   *        used if no type could be determined.
   * @return this
   */
  @Nonnull
  IJSONObject setProperty (@Nonnull String sName, @Nullable Object aValue, boolean bUseTypeConverter);

  /**
   * Sets the passed string property if the value is not <code>null</code>.
   * Empty values will still be set.
   *
   * @param sName
   *        The property name
   * @param sValue
   *        The value to set, may be <code>null</code>
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable String sValue);

  /**
   * Sets the passed string property if the value is not <code>null</code>.
   * Empty values will be set according to the passed flag.
   *
   * @param sName
   *        The property name
   * @param sValue
   *        The value to set, may be <code>null</code>
   * @param bHandleEmptyValue
   *        Whether or not empty values should be set, default is
   *        <code>true</code>
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable String sValue, boolean bHandleEmptyValue);

  /**
   * Sets the passed object property if the value is not <code>null</code>.
   * Empty object values will still be set.
   *
   * @param sName
   *        The property name
   * @param aValue
   *        The value to set, may be <code>null</code>
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable IJSONObject aValue);

  /**
   * Sets the passed object property if the value is not <code>null</code>.
   * Empty values will be set according to the passed flag.
   *
   * @param sName
   *        The property name
   * @param aValue
   *        The value to set, may be <code>null</code>
   * @param bHandleEmptyValue
   *        Whether or not empty values should be set, default is
   *        <code>true</code>
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable IJSONObject aValue, boolean bHandleEmptyValue);

  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable List <IJSONObject> aValue);

  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable List <IJSONObject> aValue, boolean bEmitEmptyValue);

  /**
   * Sets the passed boolean value.
   *
   * @param sName
   *        The property name
   * @param bValue
   *        The value to set
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, boolean bValue);

  /**
   * Sets the passed integer value.
   *
   * @param sName
   *        The property name
   * @param nValue
   *        The value to set
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, int nValue);

  /**
   * Sets the passed integer value if it is non-null.
   *
   * @param sName
   *        The property name
   * @param aValue
   *        The value to set, may be <code>null</code>
   * @return this for chaining, never <code>null</code>
   */
  @Nonnull
  JSONObject set (@Nonnull @Nonempty String sName, @Nullable Integer aValue);

  /**
   * Tries to resolve a property with the passed name and if found, removes the
   * property
   *
   * @param sName
   *        The name of the property to be removed
   * @return {@link EChange}
   */
  @Nonnull
  EChange removeProperty (@Nullable String sName);

  /**
   * Applies all properties from the passed object to this object
   *
   * @param aObjectToApply
   *        Object to apply on this
   * @return Whether or not any property was set
   */
  EChange apply (@Nullable IJSONObject aObjectToApply);

  /**
   * Applies the specified property from the passed object to this object if
   * that property is found and not null in the passed object
   *
   * @param aObjectToApply
   *        The object from which to read the property
   * @param sPropertyName
   *        The property to apply
   * @return Whether or not the property was set
   */
  EChange apply (@Nullable IJSONObject aObjectToApply, @Nonnull @Nonempty String sPropertyName);

  /**
   * @return <code>true</code> if this object contains not parse-able property
   *         values
   */
  boolean containsNotParsableProperty ();

  /**
   * @return <code>true</code> if no property is contained, <code>false</code>
   *         it at least one property is contained.
   */
  boolean isEmpty ();

  /**
   * @return The number of contained properties. Alway &ge; 0.
   */
  @Nonnegative
  int getPropertyCount ();

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  IJSONObject getClone ();
}
