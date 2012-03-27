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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.json.impl.JSONParsingException;
import com.phloc.json.impl.JSONReader;

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
   */
  @Nullable
  public static JsonNode parseToNode (@Nonnull final String sJSON) throws JSONParsingException
  {
    try
    {
      return createObjectMapper ().readTree (sJSON);
    }
    catch (final JsonProcessingException e)
    {
      throw new IllegalArgumentException ("Error parsing tree " + sJSON, e);
    }
    catch (final IOException e)
    {
      throw new IllegalArgumentException ("Error parsing tree " + sJSON, e);
    }
  }

  /**
   * Parse the passed JSON string into a {@link JsonNode} structure for further
   * processing
   * 
   * @param aIS
   *        the JSON input stream to convert, can be any valid JSON mark-up
   * @return the resulting JSON node structure
   * @throws JSONParsingException
   */
  @Nullable
  public static JsonNode parseToNode (@Nonnull final InputStream aIS) throws JSONParsingException
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");
    try
    {
      return createObjectMapper ().readTree (aIS);
    }
    catch (final JsonProcessingException e)
    {
      throw new IllegalArgumentException ("Error parsing JSON tree from InputStream", e);
    }
    catch (final IOException e)
    {
      throw new IllegalArgumentException ("Error parsing JSON tree from InputStream", e);
    }
  }

  @Nonnull
  public static IJSON parse (@Nonnull final IInputStreamProvider aISP) throws JSONParsingException
  {
    return JSONReader.parse (aISP.getInputStream ());
  }
}
