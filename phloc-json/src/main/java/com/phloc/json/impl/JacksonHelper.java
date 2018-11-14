/**
 * Copyright (C) 2006-2014 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.phloc.commons.io.streams.NonBlockingStringReader;

/**
 * Utility class around the Jackson JSON implementation
 * 
 * @author Philip Helger
 */
@Immutable
public final class JacksonHelper
{
  private JacksonHelper ()
  {}

  @Nonnull
  public static ObjectMapper createObjectMapper ()
  {
    final ObjectMapper aObjectMapper = new ObjectMapper ();
    // Necessary configuration to allow control characters inside of JSON
    // strings (CR#10516)
    aObjectMapper.configure (JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    // Feature that determines whether parser will allow use of unquoted field
    // names (which is allowed by Javascript, but not by JSON specification).
    aObjectMapper.configure (JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    // Always use BigDecimal
    aObjectMapper.enable (DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    // As of 2.1.4 BigDecimals are compacted by default - with this method
    // everything stays as it was
    aObjectMapper.setNodeFactory (JsonNodeFactory.withExactBigDecimals (true));
    return aObjectMapper;
  }

  /**
   * Parse the passed JSON string into a {@link JsonNode} structure for further
   * processing
   * 
   * @param sJSON
   *        the JSON string to convert, can be any valid JSON mark-up
   * @return the resulting JSON node structure
   * @throws JSONParsingException
   *         in case parsing failed
   */
  @Nonnull
  public static JsonNode parseToNode (@Nonnull final String sJSON) throws JSONParsingException
  {
    try
    {
      final JsonNode aNode = createObjectMapper ().readTree (new NonBlockingStringReader (sJSON));
      if (aNode == null)
      {
        throw new JSONParsingException ("no conent to map");
      }
      return aNode;
    }
    catch (final Throwable t)
    {
      throw new JSONParsingException ("Error parsing as JSON tree: '" + sJSON + "'", t); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * Parse the passed {@link InputStream} into a {@link JsonNode} structure for
   * further processing
   * 
   * @param aIS
   *        the JSON input stream to convert, can be any valid JSON mark-up
   * @return the resulting JSON node structure
   * @throws JsonReadException
   *         in case parsing failed
   */
  @Nonnull
  public static JsonNode parseToNode (@Nonnull @WillClose final InputStream aIS) throws JSONParsingException
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream"); //$NON-NLS-1$
    try
    {
      return createObjectMapper ().readTree (aIS);
    }
    catch (final Throwable t)
    {
      throw new JSONParsingException ("Error parsing as JSON tree from InputStream", t); //$NON-NLS-1$
    }
  }

  /**
   * Parse the passed {@link Reader} into a {@link JsonNode} structure for
   * further processing
   * 
   * @param aReader
   *        the JSON Reader to convert, can be any valid JSON mark-up
   * @return the resulting JSON node structure
   * @throws JsonReadException
   *         in case parsing failed
   */
  @Nonnull
  public static JsonNode parseToNode (@Nonnull @WillClose final Reader aReader) throws JSONParsingException
  {
    if (aReader == null)
      throw new NullPointerException ("reader"); //$NON-NLS-1$
    try
    {
      return createObjectMapper ().readTree (aReader);
    }
    catch (final Throwable t)
    {
      throw new JSONParsingException ("Error parsing as JSON tree from Reader", t); //$NON-NLS-1$
    }
  }
}
