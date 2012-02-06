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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

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
  private JSONReader ()
  {
    // private
  }

  @Nullable
  private static IJSON _convert (@Nonnull final JsonNode aRootNode) throws JSONParsingException
  {
    if (aRootNode.isObject ())
      return _parseObjectInternal (aRootNode);

    if (aRootNode.isArray ())
      return _parseArrayInternal (aRootNode);

    if (aRootNode.isBoolean ())
      return new JSONPropertyValueBoolean (aRootNode.getBooleanValue ());

    if (aRootNode.isBigInteger ())
      return new JSONPropertyValueBigInteger (aRootNode.getBigIntegerValue ());

    if (aRootNode.isLong ())
      return new JSONPropertyValueLong (aRootNode.getLongValue ());

    if (aRootNode.isInt ())
      return new JSONPropertyValueInteger (aRootNode.getIntValue ());

    if (aRootNode.isBigDecimal ())
      return new JSONPropertyValueBigDecimal (aRootNode.getDecimalValue ());

    if (aRootNode.isDouble ())
      return new JSONPropertyValueDouble (aRootNode.getDoubleValue ());

    if (aRootNode.isTextual ())
      return new JSONPropertyValueString (aRootNode.getTextValue ());

    return null;
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
    final IJSON ret = _convert (aRootNode);
    if (ret == null)
      throw new JSONParsingException ("Don't know how to parse JSON:" + sJSON);
    return ret;
  }

  @Nonnull
  public static IJSON parse (@Nonnull final InputStream aIS) throws JSONParsingException
  {
    final JsonNode aRootNode = JacksonHelper.parseToNode (aIS);
    final IJSON ret = _convert (aRootNode);
    if (ret == null)
      throw new JSONParsingException ("Don't know how to parse JSON from InputStream " + aIS);
    return ret;
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
   * @throws JSONParsingException
   */
  @Nonnull
  public static IJSONObject parseObject (@Nonnull final String sJSON) throws JSONParsingException
  {
    try
    {
      return _parseObjectInternal (JacksonHelper.parseToNode (sJSON));
    }
    catch (final JSONParsingException e)
    {
      throw new IllegalArgumentException ("Error parsing tree " + sJSON, e);
    }
  }

  /**
   * Parse the passed JSON array into an {@link IJSONPropertyValueList}
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON array
   *        representation!
   * @return the resulting list. The inner list type will be decided depending
   *         on the type of the first item in the array
   * @throws JSONParsingException
   */
  @Nonnull
  public static IJSONPropertyValueList <?> parseArray (@Nonnull final String sJSON) throws JSONParsingException
  {
    try
    {
      return _parseArrayInternal (JacksonHelper.parseToNode (sJSON));
    }
    catch (final JSONParsingException e)
    {
      throw new IllegalArgumentException ("Error parsing tree " + sJSON, e);
    }
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
      aList.addValue (new JSONPropertyValueBoolean (aValue.getBooleanValue ()));
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
      aList.addValue (new JSONPropertyValueInteger (aValue.getIntValue ()));
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
      aList.addValue (new JSONPropertyValueString (aValue.getTextValue ()));
    return aList;
  }

  /**
   * Utility method as specialization of {@link #getSubList(ArrayNode)} handling
   * arrays with inner array member type {@link IJSONObject}.
   * 
   * @param aValues
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   * @throws JSONParsingException
   */
  @Nonnull
  public static JSONPropertyValueList <IJSONObject> getObjectList (final ArrayNode aValues) throws JSONParsingException
  {
    final JSONPropertyValueList <IJSONObject> aList = new JSONPropertyValueList <IJSONObject> ();
    for (final JsonNode aValue : aValues)
      aList.addValue (new JSONPropertyValueJSONObject (JSONObject.fromJSONNode (aValue)));
    return aList;
  }

  /**
   * Utility method for converting a nested array (array also having inner type
   * array) into a corresponding {@link JSONPropertyValueList}
   * 
   * @param <T>
   *        the common super type (defaults to Object)
   * @param aValues
   *        the {@link ArrayNode} to convert
   * @return the resulting {@link JSONPropertyValueList}
   * @throws JSONParsingException
   */
  @SuppressWarnings ("unchecked")
  public static <T> JSONPropertyValueList <T> getSubList (@Nonnull final ArrayNode aValues) throws JSONParsingException
  {
    final JSONPropertyValueList <T> aList = new JSONPropertyValueList <T> ();
    for (final JsonNode aInnerList : aValues)
    {
      if (aInnerList.isArray ())
      {
        if (aInnerList.getElements ().hasNext ())
          aList.addValue ((IJSONPropertyValue <T>) JSONPropertyValueList.fromJSONNode ((ArrayNode) aInnerList));
        else
          aList.addValue ((IJSONPropertyValue <T>) new JSONPropertyValueList <Object> ());
      }
    }
    return aList;
  }

  /**
   * Parse the passed JSON string into an {@link IJSONObject}
   * 
   * @param aRootNode
   *        the JSON root node to convert, must be representing a JSON object!
   * @return the resulting object
   * @throws JSONParsingException
   */
  @Nonnull
  private static IJSONObject _parseObjectInternal (@Nonnull final JsonNode aRootNode) throws JSONParsingException
  {
    if (!aRootNode.isObject ())
      throw new JSONParsingException ("Passed JSON node does not represent an object!");

    return JSONObject.fromJSONNode (aRootNode);
  }

  /**
   * Parse the passed JSON array into an {@link IJSONPropertyValueList}
   * 
   * @param aRootNode
   *        the JSON root node to convert, must be representing a JSON array!
   * @return the resulting list. The inner list type will be decided depending
   *         on the type of the first item in the array
   * @throws JSONParsingException
   */
  @Nonnull
  private static IJSONPropertyValueList <?> _parseArrayInternal (@Nonnull final JsonNode aRootNode) throws JSONParsingException
  {
    if (!aRootNode.isArray ())
      throw new JSONParsingException ("Passed JSON node does not represent an array!");
    return JSONPropertyValueList.fromJSONNode ((ArrayNode) aRootNode);
  }
}
