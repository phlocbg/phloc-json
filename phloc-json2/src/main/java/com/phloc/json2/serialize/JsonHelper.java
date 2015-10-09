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
package com.phloc.json2.serialize;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * Some utility methods for Json
 * 
 * @author Philip Helger
 */
@Immutable
public final class JsonHelper
{
  public static final char MASK_CHAR = '\\';
  private static final char [] CHARS_TO_MASK = new char [] { '\0', '"', '\\', '\b', '\t', '\r', '\n', '\f' };

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final JsonHelper s_aInstance = new JsonHelper ();

  private JsonHelper ()
  {}

  private static void _escape (@Nonnull final char [] aInput, @Nonnull final StringBuilder aSB)
  {
    for (final char cCurrent : aInput)
    {
      switch (cCurrent)
      {
        case '\0':
          aSB.append (MASK_CHAR).append ("u0000");
          break;
        case '"':
          // single quotes must NOT be escaped in valid JSON (See
          // http://www.json.org/)
          // #case '\'':
        case '\\':
          aSB.append (MASK_CHAR).append (cCurrent);
          break;
        case '\b':
          aSB.append (MASK_CHAR).append ('b');
          break;
        case '\t':
          aSB.append (MASK_CHAR).append ('t');
          break;
        case '\n':
          aSB.append (MASK_CHAR).append ('n');
          break;
        case '\r':
          aSB.append (MASK_CHAR).append ('r');
          break;
        case '\f':
          aSB.append (MASK_CHAR).append ('f');
          break;
        default:
          aSB.append (cCurrent);
          break;
      }
    }
  }

  @Nullable
  public static String jsonEscape (@Nullable final String sInput)
  {
    if (StringHelper.hasNoText (sInput))
      return sInput;

    final char [] aInput = sInput.toCharArray ();
    if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
      return sInput;

    final StringBuilder aSB = new StringBuilder (aInput.length * 2);
    _escape (aInput, aSB);
    return aSB.toString ();
  }

  public static void jsonEscape (@Nullable final String sInput, @Nonnull final StringBuilder aSB)
  {
    if (StringHelper.hasText (sInput))
    {
      final char [] aInput = sInput.toCharArray ();
      if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
        aSB.append (sInput);
      else
        _escape (aInput, aSB);
    }
  }

  private static void _escape (@Nonnull final char [] aInput, @Nonnull @WillNotClose final Writer aWriter) throws IOException
  {
    for (final char cCurrent : aInput)
    {
      switch (cCurrent)
      {
        case '\0':
          aWriter.append (MASK_CHAR);
          aWriter.write ("u0000");
          break;
        case '"':
          // single quotes must NOT be escaped in valid JSON (See
          // http://www.json.org/)
          // #case '\'':
        case '\\':
          aWriter.append (MASK_CHAR);
          aWriter.append (cCurrent);
          break;
        case '\b':
          aWriter.append (MASK_CHAR);
          aWriter.append ('b');
          break;
        case '\t':
          aWriter.append (MASK_CHAR);
          aWriter.append ('t');
          break;
        case '\n':
          aWriter.append (MASK_CHAR);
          aWriter.append ('n');
          break;
        case '\r':
          aWriter.append (MASK_CHAR);
          aWriter.append ('r');
          break;
        case '\f':
          aWriter.append (MASK_CHAR);
          aWriter.append ('f');
          break;
        default:
          aWriter.append (cCurrent);
          break;
      }
    }
  }

  public static void jsonEscape (@Nullable final String sInput, @Nonnull @WillNotClose final Writer aWriter) throws IOException
  {
    if (StringHelper.hasText (sInput))
    {
      final char [] aInput = sInput.toCharArray ();
      if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
        aWriter.write (aInput, 0, aInput.length);
      else
        _escape (aInput, aWriter);
    }
  }
}
