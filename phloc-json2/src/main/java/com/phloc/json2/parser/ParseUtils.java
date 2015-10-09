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
package com.phloc.json2.parser;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * This class is used by the generated parsers to do some common stuff.
 * 
 * @author Philip Helger
 */
@Immutable
public final class ParseUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ParseUtils s_aInstance = new ParseUtils ();

  private ParseUtils ()
  {}

  private static int _hexval (final char c)
  {
    final int ret = StringHelper.getHexValue (c);
    if (ret < 0)
      throw new IllegalArgumentException ("Illegal hex char '" + c + "'");
    return ret;
  }

  @Nonnull
  public static String unescapeString (@Nonnull final StringBuilder aSB)
  {
    final char [] aSrc = new char [aSB.length () - 2];
    // Remove quotes
    aSB.getChars (1, aSB.length () - 1, aSrc, 0);

    if (aSB.indexOf ("\\") < 0)
      return new String (aSrc);

    final int nMax = aSrc.length;
    final StringBuilder ret = new StringBuilder (nMax);
    for (int i = 0; i < nMax; ++i)
    {
      final char c = aSrc[i];
      if (c == '\\')
      {
        final char cNext = aSrc[++i];
        switch (cNext)
        {
          case '"':
          case '/':
          case '\\':
            ret.append (cNext);
            break;
          case 'b':
            ret.append ('\b');
            break;
          case 'f':
            ret.append ('\f');
            break;
          case 'n':
            ret.append ('\n');
            break;
          case 'r':
            ret.append ('\r');
            break;
          case 't':
            ret.append ('\t');
            break;
          case 'u':
          {
            final char cU1 = aSrc[++i];
            final char cU2 = aSrc[++i];
            final char cU3 = aSrc[++i];
            final char cU4 = aSrc[++i];
            ret.append ((char) (_hexval (cU1) << 12 | _hexval (cU2) << 8 | _hexval (cU3) << 4 | _hexval (cU4)));
            break;
          }
          default:
            throw new IllegalArgumentException ("Unexpected escape sequence: \\" + cNext);
        }
      }
      else
        ret.append (c);
    }
    return ret.toString ();
  }
}
