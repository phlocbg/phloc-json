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
package com.phloc.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;
import com.phloc.json.impl.JSONReader;

/**
 * A basic JSON object
 * 
 * @author Boris Gregorcic
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
   * @return the Boolean value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  Boolean getBooleanProperty (@Nullable String sName);

  /**
   * An easy way to set a new boolean property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   *        Property name
   * @param bDataValue
   *        property value
   */
  @Nonnull
  IJSONObject setBooleanProperty (@Nonnull String sName, boolean bDataValue);

  @Nonnull
  IJSONObject setBooleanProperty (@Nonnull String sName, @Nonnull Boolean aDataValue);

  /**
   * Tries to resolve the value of the passed double property. If not found or
   * the property with the passed name is not of type <code>Double</code>,
   * <code>null</code> will be returned.
   * 
   * @param sName
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
   * @param dDataValue
   * @return this
   */
  @Nonnull
  IJSONObject setDoubleProperty (@Nonnull String sName, double dDataValue);

  @Nonnull
  IJSONObject setDoubleProperty (@Nonnull String sName, @Nonnull Double aDataValue);

  /**
   * Tries to resolve the value of the passed integer property. If not found or
   * the property with the passed name is not of type <code>Integer</code>,
   * <code>null</code> will be returned.
   * 
   * @param sName
   * @return the Integer value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  Integer getIntegerProperty (@Nullable String sName);

  /**
   * An easy way to set a new int property in a JSON object. Existing properties
   * with the same name will be replaced!
   * 
   * @param sName
   * @param nDataValue
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
   * @param nDataValue
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
   */
  @Nonnull
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
   */
  @Nonnull
  IJSONObject setFunctionPrebuildProperty (@Nonnull String sName, @Nonnull String sFunctionCode);

  /**
   * Tries to resolve the value of the passed list property. If not found or the
   * property with the passed name is not of type
   * <code>IJSONPropertyValueList <?></code>, <code>null</code> will returned.
   * 
   * @param sName
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
   * @param aList
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
   * @return the String value of the property with the passed name, or
   *         <code>null</code>
   */
  @Nullable
  String getStringProperty (@Nullable String sName);

  /**
   * An easy way to set a new string property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   * @param sDataValue
   * @return this
   */
  @Nonnull
  IJSONObject setStringProperty (@Nonnull String sName, @Nonnull String sDataValue);

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
   * <code>IJSONPropertyValueList <?></code>, <code>null</code> will returned.
   * 
   * @param sName
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
   * @param aObjectList
   * @return this
   */
  @Nonnull
  IJSONObject setObjectListProperty (@Nonnull String sName, @Nonnull Iterable <? extends IJSONObject> aObjectList);

  /**
   * An easy way to set a new nested list (array of string arrays) property in a
   * JSON object. Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aListOfList
   * @return this
   */
  @Nonnull
  IJSONObject setListOfListProperty (@Nonnull String sName, @Nonnull Iterable <? extends Iterable <String>> aListOfList);

  /**
   * An easy way to set a new string list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aStringList
   * @return this
   */
  @Nonnull
  IJSONObject setStringListProperty (@Nonnull String sName, @Nonnull Iterable <String> aStringList);

  /**
   * An easy way to set a new integer list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aIntList
   * @return this
   */
  @Nonnull
  IJSONObject setIntegerListProperty (@Nonnull String sName, @Nonnull int [] aIntList);

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
   * @return <code>true</code> if this object contains not parsable property
   *         values
   */
  boolean containsNotParsableProperty ();

  /**
   * @return <code>true</code> if no property is contained
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
  @Nonnull
  IJSONObject getClone ();
}
