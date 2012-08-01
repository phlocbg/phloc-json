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

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.JacksonHelper;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBigInteger;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueDouble;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueJSONObject;
import com.phloc.json.impl.value.JSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueLong;
import com.phloc.json.impl.value.JSONPropertyValueString;

/**
 * The JSONReader can be used to parse an existing JSON string into the
 * corresponding JSON object structure.
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class JSONReader
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JSONReader.class);

  private JSONReader ()
  {
    // private
  }

  @Nullable
  public static IJSON convert (@Nonnull final JsonNode aNode)
  {
    if (aNode.isObject ())
      return convertObject (aNode);

    if (aNode.isArray ())
      return convertArray ((ArrayNode) aNode);

    // boolean
    if (aNode.isBoolean ())
      return new JSONPropertyValueBoolean (aNode.booleanValue ());

    // ints
    if (aNode.isInt ())
      return new JSONPropertyValueInteger (aNode.intValue ());

    if (aNode.isLong ())
      return new JSONPropertyValueLong (aNode.longValue ());

    if (aNode.isBigInteger ())
      return new JSONPropertyValueBigInteger (aNode.bigIntegerValue ());

    // floating points
    if (aNode.isDouble ())
      return new JSONPropertyValueDouble (aNode.doubleValue ());

    if (aNode.isBigDecimal ())
      return new JSONPropertyValueBigDecimal (aNode.decimalValue ());

    // Text
    if (aNode.isTextual ())
      return new JSONPropertyValueString (aNode.textValue ());

    if (!aNode.isNull ())
      s_aLogger.info ("Having JSON Node with weird type: " + aNode);

    return null;
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JSONObject}
   * 
   * @param aNode
   * @return the resulting object
   */
  @Nonnull
  public static JSONObject convertObject (@Nonnull final JsonNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (!aNode.isObject ())
      throw new IllegalArgumentException ("node is not a JSON object");

    final JSONObject aObj = new JSONObject ();
    for (final Map.Entry <String, JsonNode> aEntry : IterableIterator.create (aNode.fields ()))
    {
      final String sFieldName = aEntry.getKey ();
      final JsonNode aValue = aEntry.getValue ();
      final IJSON aConvertedValue = convert (aValue);

      // Do not set null values
      if (aConvertedValue != null)
        aObj.setProperty (sFieldName, aConvertedValue);
    }
    return aObj;
  }

  /**
   * Converts the passed {@link ArrayNode} to a corresponding
   * {@link JSONPropertyValueList}
   * 
   * @param aValues
   * @return a property list representing the passed array
   */
  @Nonnull
  public static JSONPropertyValueList <?> convertArray (final ArrayNode aValues)
  {
    // FIXME this is extremely bogus!
    // check the first token to determine list data type
    final JsonNode aFirst = ContainerHelper.getFirstElement (aValues);
    if (aFirst != null)
    {
      if (aFirst.isObject ())
        return getObjectList (aValues);
      if (aFirst.isArray ())
        return getSubList (aValues);
      if (aFirst.isBoolean ())
        return getBooleanList (aValues);
      if (aFirst.isInt ())
        return getIntegerList (aValues);
      if (aFirst.isTextual ())
        return getStringList (aValues);
      s_aLogger.warn ("Unhandled value type: " + aFirst.getClass ());
    }
    // empty list, we cannot tell the type, so use Object
    return new JSONPropertyValueList <Object> ();
  }

  /**
   * Parse the passed JSON string into whatever resulting {@link IJSON}
   * representation
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON string!
   * @return the resulting IJSON representation
   * @throws JSONParsingException
   */
  @Nonnull
  public static IJSON parse (@Nonnull final String sJSON) throws JSONParsingException
  {
    final JsonNode aRootNode = JacksonHelper.parseToNode (sJSON);
    final IJSON aJSON = convert (aRootNode);
    if (aJSON == null)
      throw new JSONParsingException ("Don't know how to parse JSON:" + sJSON);
    return aJSON;
  }

  @Nonnull
  public static IJSON parse (@Nonnull final InputStream aIS) throws JSONParsingException
  {
    final JsonNode aRootNode = JacksonHelper.parseToNode (aIS);
    final IJSON aJSON = convert (aRootNode);
    if (aJSON == null)
      throw new JSONParsingException ("Don't know how to parse JSON from InputStream " + aIS);
    return aJSON;
  }

  @Nonnull
  public static IJSON parse (@Nonnull final IInputStreamProvider aIIS) throws JSONParsingException
  {
    return parse (aIIS.getInputStream ());
  }

  /**
   * Parse the passed JSON string into an {@link IJSONObject}
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON object
   *        representation!
   * @return the resulting object
   * @throws IllegalArgumentException
   *         in case parsing fails
   */
  @Nonnull
  public static IJSONObject parseObject (@Nonnull final String sJSON)
  {
    final JsonNode aParsedNode = JacksonHelper.parseToNode (sJSON);
    if (!aParsedNode.isObject ())
      throw new IllegalArgumentException ("Passed JSON string is not an object: '" + sJSON + "'");
    return convertObject (aParsedNode);
  }

  /**
   * Parse the passed JSON array into an {@link IJSONPropertyValueList}
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON array
   *        representation!
   * @return the resulting list. The inner list type will be decided depending
   *         on the type of the first item in the array
   * @throws IllegalArgumentException
   *         in case parsing fails
   */
  @Nonnull
  public static IJSONPropertyValueList <?> parseArray (@Nonnull final String sJSON)
  {
    final JsonNode aParsedNode = JacksonHelper.parseToNode (sJSON);
    if (!aParsedNode.isArray ())
      throw new IllegalArgumentException ("Passed JSON string is not an array: '" + sJSON + "'");
    return convertArray ((ArrayNode) aParsedNode);
  }

  /**
   * Utility method as specialization of {@link #getSubList(ArrayNode)} handling
   * arrays with inner array member type {@link Boolean}.
   * 
   * @param aValues
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   */
  @Nonnull
  public static JSONPropertyValueList <Boolean> getBooleanList (final ArrayNode aValues)
  {
    final JSONPropertyValueList <Boolean> aList = new JSONPropertyValueList <Boolean> ();
    for (final JsonNode aValue : aValues)
      aList.addValue (new JSONPropertyValueBoolean (aValue.booleanValue ()));
    return aList;
  }

  /**
   * Utility method as specialization of {@link #getSubList(ArrayNode)} handling
   * arrays with inner array member type {@link Integer}.
   * 
   * @param aValues
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   */
  @Nonnull
  public static JSONPropertyValueList <Integer> getIntegerList (final ArrayNode aValues)
  {
    final JSONPropertyValueList <Integer> aList = new JSONPropertyValueList <Integer> ();
    for (final JsonNode aValue : aValues)
      aList.addValue (new JSONPropertyValueInteger (aValue.intValue ()));
    return aList;
  }

  /**
   * Utility method as specialization of {@link #getSubList(ArrayNode)} handling
   * arrays with inner array member type {@link String}.
   * 
   * @param aValues
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   */
  @Nonnull
  public static JSONPropertyValueList <String> getStringList (final ArrayNode aValues)
  {
    final JSONPropertyValueList <String> aList = new JSONPropertyValueList <String> ();
    for (final JsonNode aValue : aValues)
      aList.addValue (new JSONPropertyValueString (aValue.textValue ()));
    return aList;
  }

  /**
   * Utility method as specialization of {@link #getSubList(ArrayNode)} handling
   * arrays with inner array member type {@link IJSONObject}.
   * 
   * @param aValues
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   */
  @Nonnull
  public static JSONPropertyValueList <IJSONObject> getObjectList (final ArrayNode aValues)
  {
    final JSONPropertyValueList <IJSONObject> aList = new JSONPropertyValueList <IJSONObject> ();
    for (final JsonNode aValue : aValues)
      aList.addValue (new JSONPropertyValueJSONObject (convertObject (aValue)));
    return aList;
  }

  /**
   * Utility method for converting a nested array (array also having inner type
   * array) into a corresponding {@link JSONPropertyValueList}
   * 
   * @param <T>
   *        the common super type (defaults to Object)
   * @param aNode
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   */
  @SuppressWarnings ("unchecked")
  public static <T> JSONPropertyValueList <T> getSubList (@Nonnull final ArrayNode aNode)
  {
    final JSONPropertyValueList <T> aList = new JSONPropertyValueList <T> ();
    for (final JsonNode aChildNode : aNode)
    {
      if (aChildNode.isArray ())
      {
        if (aChildNode.elements ().hasNext ())
          aList.addValue ((IJSONPropertyValue <T>) convertArray ((ArrayNode) aChildNode));
        else
          aList.addValue ((IJSONPropertyValue <T>) new JSONPropertyValueList <Object> ());
      }
    }
    return aList;
  }
}
