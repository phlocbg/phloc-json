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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * Some utility methods for JSON
 * 
 * @author philip
 */
@Immutable
public final class JSONHelper
{
  private static final char [] CHARS_TO_MASK = new char [] { '"', '\\', '\b', '\t', '\r', '\n', '\f' };
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

    final char [] ret = new char [aInput.length * 2];
    int nIndex = 0;
    for (final char cCurrent : aInput)
    {
      switch (cCurrent)
      {
        case '"':
          // single quotes must NOT be escaped in valid JSON (See
          // http://www.json.org/)
          // #case '\'':
        case '\\':
          ret[nIndex++] = MASK_CHAR;
          ret[nIndex++] = cCurrent;
          break;
        case '\b':
          ret[nIndex++] = MASK_CHAR;
          ret[nIndex++] = 'b';
          break;
        case '\t':
          ret[nIndex++] = MASK_CHAR;
          ret[nIndex++] = 't';
          break;
        case '\n':
          ret[nIndex++] = MASK_CHAR;
          ret[nIndex++] = 'n';
          break;
        case '\r':
          ret[nIndex++] = MASK_CHAR;
          ret[nIndex++] = 'r';
          break;
        case '\f':
          ret[nIndex++] = MASK_CHAR;
          ret[nIndex++] = 'f';
          break;
        default:
          ret[nIndex++] = cCurrent;
          break;
      }
    }

    return new String (ret, 0, nIndex);
  }
}
