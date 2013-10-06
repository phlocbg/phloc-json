/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.json2.serialize;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReaderProvider;
import com.phloc.json.JacksonHelper;
import com.phloc.json.impl.JSONParsingException;
import com.phloc.json2.IJson;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;
import com.phloc.json2.impl.JsonValue;

/**
 * Parse JSON string into {@link IJson} objects
 * 
 * @author Philip Helger
 */
@Immutable
public final class JsonReader
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final JsonReader s_aInstance = new JsonReader ();

  private JsonReader ()
  {}

  @Nonnull
  public static IJson convert (@Nonnull final JsonNode aNode)
  {
    if (aNode.isObject ())
      return convertObject ((ObjectNode) aNode);

    if (aNode.isArray ())
      return convertArray ((ArrayNode) aNode);

    // boolean
    if (aNode.isBoolean ())
      return JsonValue.create (aNode.booleanValue ());

    // integers
    if (aNode.isInt ())
      return JsonValue.create (aNode.intValue ());

    if (aNode.isLong ())
      return JsonValue.create (aNode.longValue ());

    if (aNode.isBigInteger ())
      return JsonValue.create (aNode.bigIntegerValue (), JsonValueSerializerToString.getInstance ());

    // floating points
    if (aNode.isDouble ())
      return JsonValue.create (aNode.doubleValue ());

    if (aNode.isBigDecimal ())
      return JsonValue.create (aNode.decimalValue (), JsonValueSerializerToString.getInstance ());

    // Text
    if (aNode.isTextual ())
      return JsonValue.create (aNode.textValue (), JsonValueSerializerEscaped.getInstance ());

    if (aNode.isNull ())
      return JsonValue.NULL;

    throw new IllegalArgumentException ("Having JSON Node with weird type: " + aNode);
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JsonObject}
   * 
   * @param aObjectNode
   *        Jackson object node
   * @return the resulting object
   */
  @Nonnull
  public static JsonObject convertObject (@Nonnull final ObjectNode aObjectNode)
  {
    if (aObjectNode == null)
      throw new NullPointerException ("node");

    final JsonObject aObj = new JsonObject (aObjectNode.size ());
    for (final Map.Entry <String, JsonNode> aEntry : IterableIterator.create (aObjectNode.fields ()))
    {
      final String sFieldName = aEntry.getKey ();
      final JsonNode aChildNode = aEntry.getValue ();
      // Recursive convert
      final IJson aConvertedChildNode = convert (aChildNode);
      aObj.add (sFieldName, aConvertedChildNode);
    }
    return aObj;
  }

  /**
   * Converts the passed {@link ArrayNode} to a corresponding {@link JsonArray}
   * 
   * @param aArrayNode
   *        JAckson array node
   * @return a property list representing the passed array
   */
  @Nonnull
  public static JsonArray convertArray (final ArrayNode aArrayNode)
  {
    final JsonArray ret = new JsonArray (aArrayNode.size ());
    for (final JsonNode aChildNode : aArrayNode)
    {
      // Recursive convert
      final IJson aConvertedChildNode = convert (aChildNode);
      ret.add (aConvertedChildNode);
    }
    return ret;
  }

  /**
   * Parse the passed JSON string into whatever resulting {@link IJson}
   * representation
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON string!
   * @return the resulting IJson representation
   * @throws JSONParsingException
   */
  @Nonnull
  public static IJson parse (@Nonnull final String sJSON) throws JSONParsingException
  {
    return convert (JacksonHelper.parseToNode (sJSON));
  }

  @Nonnull
  public static IJson parse (@Nonnull final InputStream aIS) throws JSONParsingException
  {
    return convert (JacksonHelper.parseToNode (aIS));
  }

  @Nonnull
  public static IJson parse (@Nonnull final IInputStreamProvider aIIS) throws JSONParsingException
  {
    return convert (JacksonHelper.parseToNode (aIIS.getInputStream ()));
  }

  @Nonnull
  public static IJson parse (@Nonnull final Reader aReader) throws JSONParsingException
  {
    return convert (JacksonHelper.parseToNode (aReader));
  }

  @Nonnull
  public static IJson parse (@Nonnull final IReaderProvider aReaderProvider) throws JSONParsingException
  {
    return convert (JacksonHelper.parseToNode (aReaderProvider.getReader ()));
  }
}
