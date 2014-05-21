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
package com.phloc.json2.serialize;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.json2.IJson;
import com.phloc.json2.IJsonArray;
import com.phloc.json2.IJsonObject;
import com.phloc.json2.IJsonValue;

/**
 * Convert {@link IJson} objects to a String.
 * 
 * @author Philip Helger
 */
@Immutable
public final class JsonWriter
{
  /** character opening a JSON array */
  public static final char ARRAY_START = '[';
  /** character closing a JSON array */
  public static final char ARRAY_END = ']';
  /** character opening a JSON object */
  public static final char OBJECT_START = '{';
  /** character closing a JSON object */
  public static final char OBJECT_END = '}';
  /** character used as separator items in collections */
  public static final char ITEM_SEPARATOR = ',';
  /** character used for assignments (between name and value) */
  public static final char NAME_VALUE_SEPARATOR = ':';

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final JsonWriter s_aInstance = new JsonWriter ();

  private JsonWriter ()
  {}

  public static void writeNode (@Nonnull final IJson aJson, @Nonnull @WillNotClose final Writer aWriter) throws IOException
  {
    if (aJson.isValue ())
    {
      ((IJsonValue) aJson).appendAsJsonString (aWriter);
    }
    else
      if (aJson.isArray ())
      {
        aWriter.append (ARRAY_START);
        int nIndex = 0;
        for (final IJson aChild : (IJsonArray) aJson)
        {
          if (nIndex++ > 0)
            aWriter.append (ITEM_SEPARATOR);
          writeNode (aChild, aWriter);
        }
        aWriter.append (ARRAY_END);
      }
      else
        if (aJson.isObject ())
        {
          aWriter.append (OBJECT_START);
          int nIndex = 0;
          for (final Map.Entry <String, IJson> aEntry : (IJsonObject) aJson)
          {
            if (nIndex++ > 0)
              aWriter.append (ITEM_SEPARATOR);
            JsonValueSerializerEscaped.appendEscapedJsonString (aEntry.getKey (), aWriter);
            aWriter.append (NAME_VALUE_SEPARATOR);
            writeNode (aEntry.getValue (), aWriter);
          }
          aWriter.append (OBJECT_END);
        }
        else
          throw new IllegalArgumentException ("Unsupported Json Object type: " + aJson);
  }

  public static void writeNodeAndClose (@Nonnull final IJson aJson, @Nonnull @WillClose final Writer aWriter) throws IOException
  {
    try
    {
      writeNode (aJson, aWriter);
    }
    finally
    {
      StreamUtils.close (aWriter);
    }
  }

  @Nonnull
  public static String getAsString (@Nonnull final IJson aJson)
  {
    if (aJson == null)
      throw new NullPointerException ("JSON");

    try
    {
      final NonBlockingStringWriter aWriter = new NonBlockingStringWriter (1024);
      writeNodeAndClose (aJson, aWriter);
      return aWriter.getAsString ();
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException ("NonBlockingStringWriter should never throw IOException!");
    }
  }
}
