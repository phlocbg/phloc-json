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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

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
import com.phloc.json2.IJson;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;
import com.phloc.json2.impl.JsonValue;
import com.phloc.json2.parser.JsonReader;

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
  public static IJSONPropertyValue <?> convert (@Nonnull final IJson aNode)
  {
    if (aNode.isObject ())
      return convertObject ((JsonObject) aNode);

    if (aNode.isArray ())
      return convertArray ((JsonArray) aNode);

    if (aNode.isValue ())
    {
      final JsonValue aValue = (JsonValue) aNode;
      // boolean
      if (aValue.isBooleanValue ())
        return new JSONPropertyValueBoolean (aValue.getCastedValue (Boolean.class));

      // integers
      if (aValue.isIntValue ())
      {
        final BigInteger aVal = aValue.getCastedValue (BigInteger.class);
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

      // floating points
      if (aValue.isDecimalValue ())
      {
        return new JSONPropertyValueBigDecimal (aValue.getCastedValue (BigDecimal.class));
      }
      // Text
      if (aValue.isStringValue ())
        return new JSONPropertyValueString (aValue.getCastedValue (String.class));

      // null
      if (aValue.isNullValue ())
        return new JSONPropertyValueKeyword (CJSONConstants.KEYWORD_NULL);
    }
    return null;
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JSONObject}
   * 
   * @param aNode
   * @return the resulting object
   */
  @Nonnull
  public static JSONObject convertObject (@Nonnull final JsonObject aNode)
  {
    if (aNode == null)
    {
      throw new NullPointerException ("aNode"); //$NON-NLS-1$
    }

    final JSONObject aObj = new JSONObject ();
    for (final Map.Entry <String, IJson> aEntry : aNode.getAll ().entrySet ())
    {
      final IJSON aConvertedValue = convert (aEntry.getValue ());
      // Do not set null values
      if (aConvertedValue != null)
      {
        aObj.setProperty (aEntry.getKey (), aConvertedValue);
      }
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
  public static JSONPropertyValueList <?> convertArray (final JsonArray aValues)
  {
    final JSONPropertyValueList <IJSONPropertyValue <?>> ret = new JSONPropertyValueList <IJSONPropertyValue <?>> ();
    for (final IJson aNode : aValues.getAll ())
    {
      ret.addValue (convert (aNode));
    }
    return ret;
  }

  @Nonnull
  private static IJSON convertInternal (@Nonnull final IJson aNode) throws JSONParsingException
  {
    final IJSON aJSON = convert (aNode);
    if (aJSON == null)
    {
      throw new JSONParsingException ("Failed to convert JSON node to internal representation: " + aNode); //$NON-NLS-1$
    }
    return aJSON;
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
    return convertInternal (parseJSONNonNull (sJSON));
  }

  private static IJson parseJSONNonNull (@Nonnull final String sJSON) throws JSONParsingException
  {
    final IJson aJson = JsonReader.readFromString (sJSON);
    if (aJson == null)
    {
      throw new JSONParsingException ("Failed to parse JSON: " + sJSON); //$NON-NLS-1$
    }
    return aJson;
  }

  @Nonnull
  public static IJSON parse (@Nonnull final InputStream aIS) throws JSONParsingException
  {
    return convertInternal (JsonReader.readFromStream (aIS));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final IInputStreamProvider aIIS) throws JSONParsingException
  {
    return convertInternal (JsonReader.readFromStream (aIIS.getInputStream ()));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final Reader aReader) throws JSONParsingException
  {
    return convertInternal (JsonReader.readFromReader (aReader));
  }

  @Nonnull
  public static IJSON parse (@Nonnull final IReaderProvider aReaderProvider) throws JSONParsingException
  {
    return convertInternal (JsonReader.readFromReader (aReaderProvider.getReader ()));
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
    final IJson aJson = parseJSONNonNull (sJSON);
    if (!aJson.isObject ())
    {
      throw new JSONParsingException ("Passed JSON string is not an object: '" + sJSON + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return convertObject ((JsonObject) aJson);
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
    final IJson aJson = parseJSONNonNull (sJSON);
    if (!aJson.isArray ())
    {
      throw new JSONParsingException ("Passed JSON string is not an array: '" + sJSON + "'"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return convertArray ((JsonArray) aJson);
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
