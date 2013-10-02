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
package com.phloc.json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.string.StringHelper;

/**
 * Some utility methods for JSON
 * 
 * @author Philip Helger
 */
@Immutable
public final class JSONHelper
{
  private static final char [] CHARS_TO_MASK = new char [] { '\0', '"', '\\', '\b', '\t', '\r', '\n', '\f' };
  private static final char MASK_CHAR = '\\';

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final JSONHelper s_aInstance = new JSONHelper ();

  private JSONHelper ()
  {}

  @Nullable
  public static String jsonEscape (@Nullable final String sInput)
  {
    if (StringHelper.hasNoText (sInput))
      return sInput;

    final char [] aInput = sInput.toCharArray ();
    if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
      return sInput;

    final StringBuilder aSB = new StringBuilder (aInput.length * 2);
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
    return aSB.toString ();
  }

  public static void jsonEscape (@Nullable final String sInput, @Nonnull final NonBlockingStringWriter aWriter)
  {
    if (StringHelper.hasNoText (sInput))
      return;

    final char [] aInput = sInput.toCharArray ();
    if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
    {
      aWriter.write (sInput);
      return;
    }

    for (final char cCurrent : aInput)
    {
      switch (cCurrent)
      {
        case '\0':
          aWriter.write (MASK_CHAR);
          aWriter.write ("u0000");
          break;
        case '"':
          // single quotes must NOT be escaped in valid JSON (See
          // http://www.json.org/)
          // #case '\'':
        case '\\':
          aWriter.write (MASK_CHAR);
          aWriter.write (cCurrent);
          break;
        case '\b':
          aWriter.write (MASK_CHAR);
          aWriter.write ('b');
          break;
        case '\t':
          aWriter.write (MASK_CHAR);
          aWriter.write ('t');
          break;
        case '\n':
          aWriter.write (MASK_CHAR);
          aWriter.write ('n');
          break;
        case '\r':
          aWriter.write (MASK_CHAR);
          aWriter.write ('r');
          break;
        case '\f':
          aWriter.write (MASK_CHAR);
          aWriter.write ('f');
          break;
        default:
          aWriter.write (cCurrent);
          break;
      }
    }
  }
}
