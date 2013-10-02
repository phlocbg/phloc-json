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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.JSONHelper;
import com.phloc.json2.IJsonValueSerializer;

public class JsonValueSerializerEscaped implements IJsonValueSerializer
{
  private static final JsonValueSerializerEscaped s_aInstance = new JsonValueSerializerEscaped ();

  private JsonValueSerializerEscaped ()
  {}

  @Nonnull
  public static JsonValueSerializerEscaped getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  @Nonempty
  public static String getEscapedJsonString (@Nonnull final String sValue)
  {
    final NonBlockingStringWriter aWriter = new NonBlockingStringWriter (sValue.length () * 2);
    aWriter.append ('"');
    JSONHelper.jsonEscape (sValue, aWriter);
    aWriter.append ('"');
    return aWriter.getAsString ();
  }

  @Nonnull
  @Nonempty
  public String getAsJsonString (@Nullable final Object aValue)
  {
    return getEscapedJsonString (String.valueOf (aValue));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
