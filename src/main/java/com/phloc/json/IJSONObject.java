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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;

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
   */
  void setProperty (IJSONProperty <?> aProperty);

  /**
   * Get the property with the specified name
   * 
   * @param sName
   *        name of the requested property
   * @return the corresponding {@link IJSONProperty} if found, <code>null</code>
   *         otherwise
   */
  IJSONProperty <?> getProperty (String sName);

  /**
   * Get a list of all property names of this object
   * 
   * @return the property names (according to property ordering)
   */
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
  Boolean getBooleanProperty (String sName);

  /**
   * An easy way to set a new boolean property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   * @param bDataValue
   */
  void setBooleanProperty (@Nonnull String sName, boolean bDataValue);

  void setBooleanProperty (@Nonnull String sName, @Nullable Boolean aDataValue);

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
  Double getDoubleProperty (String sName);

  /**
   * An easy way to set a new double property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   * @param dDataValue
   */
  void setDoubleProperty (String sName, double dDataValue);

  void setDoubleProperty (@Nonnull String sName, @Nullable Double aDataValue);

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
  Integer getIntegerProperty (String sName);

  /**
   * An easy way to set a new int property in a JSON object. Existing properties
   * with the same name will be replaced!
   * 
   * @param sName
   * @param nDataValue
   */
  void setIntegerProperty (String sName, int nDataValue);

  void setIntegerProperty (String sName, @Nullable Integer aDataValue);

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
  Long getLongProperty (String sName);

  /**
   * An easy way to set a new int property in a JSON object. Existing properties
   * with the same name will be replaced!
   * 
   * @param sName
   * @param nDataValue
   */
  void setLongProperty (String sName, long nDataValue);

  void setLongProperty (String sName, @Nullable Long aDataValue);

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
  String getKeywordProperty (String sName);

  /**
   * An easy way to set a new keyword property (not quoted) in a JSON object.
   * Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param sDataValue
   */
  void setKeywordProperty (String sName, String sDataValue);

  /**
   * An easy way to set a new function property (not escaped) in a JSON object.
   * The function keyword and the parenthesis for parameters as well as the body
   * braces will be automatically created.
   * 
   * @param sName
   *        The name of the function property
   * @param sBody
   *        The function body JS code
   * @param aParams
   *        The function parameter names
   */
  void setFunctionProperty (String sName, String sBody, String... aParams);

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
  <I> List <I> getListProperty (String sName);

  /**
   * An easy way to set a new list type property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   * @param aList
   */
  <DATATYPE> void setListProperty (String sName, IJSONPropertyValueList <DATATYPE> aList);

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
  IJSONObject getObjectProperty (String sName);

  /**
   * An easy way to set a new JSON object property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   * @param aObject
   *        the nested object
   */
  void setObjectProperty (String sName, IJSONObject aObject);

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
  String getStringProperty (String sName);

  /**
   * An easy way to set a new string property in a JSON object. Existing
   * properties with the same name will be replaced!
   * 
   * @param sName
   * @param sDataValue
   */
  void setStringProperty (String sName, String sDataValue);

  @Nullable
  BigDecimal getBigDecimalProperty (String sName);

  void setBigDecimalProperty (String sName, BigDecimal aDataValue);

  @Nullable
  BigInteger getBigIntegerProperty (String sName);

  void setBigIntegerProperty (String sName, BigInteger aDataValue);

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
  List <? extends IJSONPropertyValue <?>> getListValues (final String sName);

  @Nonnull
  List <IJSONObject> getObjectListProperty (@Nullable String sName);

  /**
   * An easy way to set a new JSON object list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aObjectList
   */
  void setObjectListProperty (String sName, Collection <? extends IJSONObject> aObjectList);

  /**
   * An easy way to set a new nested list (array of string arrays) property in a
   * JSON object. Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aListOfList
   */
  void setListOfListProperty (String sName, Collection <Collection <String>> aListOfList);

  /**
   * An easy way to set a new string list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aStringList
   */
  void setStringListProperty (String sName, Collection <String> aStringList);

  /**
   * An easy way to set a new integer list type property in a JSON object.
   * Existing properties with the same name will be replaced!
   * 
   * @param sName
   * @param aIntList
   */
  void setIntegerListProperty (String sName, int [] aIntList);

  /**
   * This is a helper method to set an arbitrary property not exactly knowing
   * the internal data type. As a fall-back a string property will be used if no
   * matching data type can be found
   * 
   * @param sName
   *        Property name
   * @param aValue
   *        value to be set
   */
  void setProperty (@Nonnull String sName, @Nullable Object aValue);

  /**
   * Tries to resolve a property with the passed name and if found, removes the
   * property
   * 
   * @param sName
   *        The name of the property to be removed
   */
  @Nonnull
  EChange removeProperty (final String sName);

  @Nonnull
  IJSONObject getClone ();
}
