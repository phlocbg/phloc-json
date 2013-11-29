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
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReaderProvider;
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

  /**
   * Convert the passed Jackson value node to a phloc-json2 value node
   * 
   * @param aNode
   *        The source Jackson node. May not be <code>null</code>.
   * @return <code>null</code> if the passed node cannot be converted.
   */
  @Nullable
  public static JsonValue convertValue (@Nonnull final JsonNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");

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

    return null;
  }

  /**
   * Convert the passed Jackson node to a phloc-json2 node. Supports object
   * nodes, array nodes and value nodes.
   * 
   * @param aNode
   *        The source Jackson node. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @throws JsonReadException
   *         If the passed Jackson node cannot be converted
   */
  @Nonnull
  public static IJson convert (@Nonnull final JsonNode aNode) throws JsonReadException
  {
    if (aNode == null)
      throw new NullPointerException ("node");

    if (aNode.isObject ())
      return convertObject ((ObjectNode) aNode);

    if (aNode.isArray ())
      return convertArray ((ArrayNode) aNode);

    final IJson ret = convertValue (aNode);
    if (ret == null)
      throw new JsonReadException ("Having JsonNode with unsupported type: " + aNode);
    return ret;
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JsonObject}
   * 
   * @param aObjectNode
   *        Jackson object node. May not be <code>null</code>.
   * @param aTarget
   *        The target array to be filled. May not be <code>null</code>.
   * @throws JsonReadException
   */
  private static void _convertObject (@Nonnull final ObjectNode aObjectNode, @Nonnull final JsonObject aTarget) throws JsonReadException
  {
    if (aObjectNode == null)
      throw new NullPointerException ("objectNode");
    if (aTarget == null)
      throw new NullPointerException ("target");

    for (final Map.Entry <String, JsonNode> aEntry : IterableIterator.create (aObjectNode.fields ()))
    {
      final String sFieldName = aEntry.getKey ();
      final JsonNode aChildNode = aEntry.getValue ();
      // Recursive convert
      final IJson aConvertedChildNode = convert (aChildNode);
      aTarget.add (sFieldName, aConvertedChildNode);
    }
  }

  /**
   * Converts a passed {@link JsonNode} into a {@link JsonObject}
   * 
   * @param aObjectNode
   *        Jackson object node. May not be <code>null</code>.
   * @return the resulting object. Never <code>null</code>.
   * @throws JsonReadException
   */
  @Nonnull
  public static JsonObject convertObject (@Nonnull final ObjectNode aObjectNode) throws JsonReadException
  {
    if (aObjectNode == null)
      throw new NullPointerException ("node");

    final JsonObject aObj = new JsonObject (aObjectNode.size ());
    _convertObject (aObjectNode, aObj);
    return aObj;
  }

  /**
   * Converts the passed {@link ArrayNode} to a corresponding {@link JsonArray}
   * 
   * @param aArrayNode
   *        Jackson array node. May not be <code>null</code>.
   * @param aTarget
   *        The target array to be filled. May not be <code>null</code>.
   * @throws JsonReadException
   */
  private static void _convertArray (@Nonnull final ArrayNode aArrayNode, @Nonnull final JsonArray aTarget) throws JsonReadException
  {
    if (aArrayNode == null)
      throw new NullPointerException ("arrayNode");
    if (aTarget == null)
      throw new NullPointerException ("target");

    for (final JsonNode aChildNode : aArrayNode)
    {
      // Recursive convert
      final IJson aConvertedChildNode = convert (aChildNode);
      aTarget.add (aConvertedChildNode);
    }
  }

  /**
   * Converts the passed {@link ArrayNode} to a corresponding {@link JsonArray}
   * 
   * @param aArrayNode
   *        Jackson array node. May not be <code>null</code>.
   * @return a property list representing the passed array. Never
   *         <code>null</code>.
   * @throws JsonReadException
   */
  @Nonnull
  public static JsonArray convertArray (@Nonnull final ArrayNode aArrayNode) throws JsonReadException
  {
    if (aArrayNode == null)
      throw new NullPointerException ("arrayNode");

    final JsonArray ret = new JsonArray (aArrayNode.size ());
    _convertArray (aArrayNode, ret);
    return ret;
  }

  public static void parseAsArray (@Nonnull final String sJSON, @Nonnull final JsonArray aArray) throws JsonReadException
  {
    final JsonNode aNode = JacksonHelper.parseToNode (sJSON);
    if (!aNode.isArray ())
      throw new JsonReadException ("Passed string is not a JSON array!");
    _convertArray ((ArrayNode) aNode, aArray);
  }

  public static void parseAsObject (@Nonnull final String sJSON, @Nonnull final JsonObject aObject) throws JsonReadException
  {
    final JsonNode aNode = JacksonHelper.parseToNode (sJSON);
    if (!aNode.isObject ())
      throw new JsonReadException ("Passed string is not a JSON object!");
    _convertObject ((ObjectNode) aNode, aObject);
  }

  @Nonnull
  public static JsonValue parseAsValue (@Nonnull final String sJSON) throws JsonReadException
  {
    final JsonNode aNode = JacksonHelper.parseToNode (sJSON);
    if (aNode.isContainerNode ())
      throw new JsonReadException ("Passed string is not a JSON value!");
    return convertValue (aNode);
  }

  /**
   * Parse the passed JSON string into whatever resulting {@link IJson}
   * representation
   * 
   * @param sJSON
   *        the JSON string to convert, must be a valid JSON string!
   * @return the resulting IJson representation. Never <code>null</code>.
   * @throws JsonReadException
   */
  @Nonnull
  public static IJson parse (@Nonnull final String sJSON) throws JsonReadException
  {
    return convert (JacksonHelper.parseToNode (sJSON));
  }

  @Nonnull
  public static IJson parse (@Nonnull @WillClose final InputStream aIS) throws JsonReadException
  {
    return convert (JacksonHelper.parseToNode (aIS));
  }

  @Nonnull
  public static IJson parse (@Nonnull final IInputStreamProvider aIIS) throws JsonReadException
  {
    if (aIIS == null)
      throw new NullPointerException ("InputStreamProvider");

    return convert (JacksonHelper.parseToNode (aIIS.getInputStream ()));
  }

  @Nonnull
  public static IJson parse (@Nonnull @WillClose final Reader aReader) throws JsonReadException
  {
    return convert (JacksonHelper.parseToNode (aReader));
  }

  @Nonnull
  public static IJson parse (@Nonnull final IReaderProvider aReaderProvider) throws JsonReadException
  {
    if (aReaderProvider == null)
      throw new NullPointerException ("ReaderProvider");

    return convert (JacksonHelper.parseToNode (aReaderProvider.getReader ()));
  }
}
