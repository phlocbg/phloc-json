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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReaderProvider;
import com.phloc.commons.string.StringHelper;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBigInteger;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueKeyword;
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

  @Nonnull
  public static IJSONPropertyValue <?> convertNonnull (@Nonnull final JsonNode aNode) throws JSONParsingException
  {
    final IJSONPropertyValue <?> aValue = convert (aNode);
    if (aValue == null)
    {
      throw new JSONParsingException ("Unable to convert node to JSON: '" + aNode + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return aValue;
  }

  @Nullable
  public static IJSONPropertyValue <?> convert (@Nonnull final JsonNode aNode)
  {
    if (aNode.isObject ())
      return convertObject ((ObjectNode) aNode);

    if (aNode.isArray ())
      return convertArray ((ArrayNode) aNode);

    if (aNode.isValueNode ())
    {
      final ValueNode aValue = (ValueNode) aNode;
      // boolean
      if (aValue.isBoolean ())
      {
        return new JSONPropertyValueBoolean (((BooleanNode) aValue).asBoolean ());
      }
      // integers
      if (aValue.isBigInteger ())
      {
        final BigInteger aVal = ((BigIntegerNode) aValue).bigIntegerValue ();
        // check if it fits in an integer
        final int nVal = aVal.intValue ();
        if (String.valueOf (nVal).equals (aVal.toString ()))
        {
          return new JSONPropertyValueInteger (nVal);
        }
        // check if it fits in a long
        final long nLongVal = aVal.longValue ();
        if (String.valueOf (nLongVal).equals (aVal.toString ()))
        {
          return new JSONPropertyValueLong (nLongVal);
        }
        return new JSONPropertyValueBigInteger (aVal);
      }
      if (aValue.isLong ())
      {
        final Long aVal = Long.valueOf (((LongNode) aValue).longValue ());
        // check if it fits in an integer
        final int nVal = aVal.intValue ();
        if (String.valueOf (nVal).equals (aVal.toString ()))
        {
          return new JSONPropertyValueInteger (nVal);
        }
        // check if it fits in a long
        return new JSONPropertyValueLong (aVal.longValue ());
      }
      if (aValue.isInt ())
      {
        return new JSONPropertyValueInteger (((IntNode) aValue).asInt ());
      }
      if (aValue.isShort ())
      {
        return new JSONPropertyValueInteger (((ShortNode) aValue).asInt ());
      }

      // floating points
      if (aValue.isBigDecimal ())
      {
        return new JSONPropertyValueBigDecimal (((DecimalNode) aValue).decimalValue ());
      }
      if (aValue.isDouble ())
      {
        return new JSONPropertyValueBigDecimal (((DoubleNode) aValue).decimalValue ());
      }
      if (aValue.isFloat ())
      {
        return new JSONPropertyValueBigDecimal (((FloatNode) aValue).decimalValue ());
      }

      // Text
      if (aNode.isTextual ())
      {
        return new JSONPropertyValueString (aNode.textValue ());
      }

      // null
      if (aValue.isNull () && JSONSettings.getInstance ().isParseNullValues ())
      {
        return new JSONPropertyValueKeyword (CJSONConstants.KEYWORD_NULL);
      }
    }
    return null;
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JSONObject}
   * 
   * @param aNode
   *        The node to convert, may not be <code>null</code>
   * @return the resulting object
   */
  @Nonnull
  public static JSONObject convertObject (@Nonnull final ObjectNode aNode)
  {
    if (aNode == null)
    {
      throw new NullPointerException ("aNode"); //$NON-NLS-1$
    }
    final JSONObject aObject = new JSONObject ();
    for (final Map.Entry <String, JsonNode> aEntry : IterableIterator.create (aNode.fields ()))
    {
      final String sFieldName = aEntry.getKey ();
      final JsonNode aChildNode = aEntry.getValue ();
      // Recursive convert
      final IJSONPropertyValue <?> aConvertedValue = convert (aChildNode);
      // Do not set null values
      if (aConvertedValue != null)
      {
        aObject.setProperty (sFieldName, aConvertedValue);
      }
    }
    return aObject;
  }

  /**
   * Converts the passed {@link ArrayNode} to a corresponding
   * {@link JSONPropertyValueList}
   * 
   * @param aValues
   *        The array node to convert
   * @return a property list representing the passed array
   */
  @Nonnull
  public static JSONPropertyValueList <?> convertArray (final ArrayNode aValues)
  {
    final JSONPropertyValueList <IJSONPropertyValue <?>> aArray = new JSONPropertyValueList <IJSONPropertyValue <?>> ();
    for (final JsonNode aChildNode : aValues)
    {
      // Recursive convert
      final IJSONPropertyValue <?> aConvertedValue = convert (aChildNode);
      if (aConvertedValue != null)
      {
        aArray.addValue (aConvertedValue);
      }
    }
    return aArray;
  }

  /**
   * Parse the passed JSON string into whatever resulting {@link IJSON}
   * representation
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON string!
   * @return the resulting IJSON representation
   * @throws JSONParsingException
   *         in case parsing fails
   */
  @Nonnull
  public static IJSON parse (@Nonnull final String sJSON) throws JSONParsingException
  {
    return parseJSONNonNull (sJSON);
  }

  private static IJSON parseJSONNonNull (@Nonnull final String sJSON) throws JSONParsingException
  {
    return convertNonnull (JacksonHelper.parseToNode (sJSON));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final InputStream aIS) throws JSONParsingException
  {
    return convertNonnull (JacksonHelper.parseToNode (aIS));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final IInputStreamProvider aIIS) throws JSONParsingException
  {
    return convertNonnull (JacksonHelper.parseToNode (aIIS.getInputStream ()));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final Reader aReader) throws JSONParsingException
  {
    return convertNonnull (JacksonHelper.parseToNode (aReader));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final IReaderProvider aReaderProvider) throws JSONParsingException
  {
    return convertNonnull (JacksonHelper.parseToNode (aReaderProvider.getReader ()));
  }

  /**
   * Parse the passed JSON string into an {@link IJSONObject}
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON object
   *        representation!
   * @return the resulting object
   * @throws JSONParsingException
   *         in case parsing fails
   */
  @Nonnull
  public static IJSONObject parseObject (@Nonnull final String sJSON) throws JSONParsingException
  {
    final IJSON aJSON = parseJSONNonNull (sJSON);
    if (!(aJSON instanceof IJSONObject))
    {
      throw new JSONParsingException ("Passed JSON string is not an object: '" + sJSON + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return (IJSONObject) aJSON;
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
   *         in case parsing fails
   */
  @Nonnull
  public static IJSONPropertyValueList <?> parseArray (@Nonnull final String sJSON) throws JSONParsingException
  {
    final IJSON aJSON = parseJSONNonNull (sJSON);
    if (!(aJSON instanceof IJSONPropertyValueList))
    {
      throw new JSONParsingException ("Passed JSON string is not an array: '" + sJSON + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return (IJSONPropertyValueList <?>) aJSON;
  }

  /**
   * Parse the passed JSON array into an {@link IJSONPropertyValueList} if
   * possible. This is a safe wrapper around {@link #parseArray(String)} that
   * just returns <code>null</code> in case of an error and does not throw
   * exceptions.
   * 
   * @param sJSON
   *        the JSON string to convert
   * @return the resulting list. The inner list type will be decided depending
   *         on the type of the first item in the array
   */
  public static IJSONPropertyValueList <?> parseArraySafe (final String sJSON)
  {
    if (StringHelper.hasNoText (sJSON))
    {
      return null;
    }
    try
    {
      return JSONReader.parseArray (sJSON);
    }
    catch (final JSONParsingException aEx)
    {
      return null;
    }
  }
}
