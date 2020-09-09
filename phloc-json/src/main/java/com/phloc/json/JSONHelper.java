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
package com.phloc.json;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.StringHelper;

/**
 * Some utility methods for JSON
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class JSONHelper
{
  public static final char MASK_CHAR = '\\';
  // http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf

  protected static final char [] CHARS_TO_MASK = new char [] { '\u0000',
                                                               '\u0001',
                                                               '\u0002',
                                                               '\u0003',
                                                               '\u0004',
                                                               '\u0005',
                                                               '\u0006',
                                                               '\u0007',
                                                               // \u0008--> \b
                                                               // \u0009--> \t
                                                               // u000A--> \n
                                                               '\u000B',
                                                               // \u000C--> \f
                                                               // u000D--> \r
                                                               '\u000E',
                                                               '\u000F',
                                                               '\u0010',
                                                               '\u0011',
                                                               '\u0012',
                                                               '\u0013',
                                                               '\u0014',
                                                               '\u0015',
                                                               '\u0016',
                                                               '\u0017',
                                                               '\u0018',
                                                               '\u0019',
                                                               '\u001A',
                                                               '\u001B',
                                                               '\u001C',
                                                               '\u001D',
                                                               '\u001E',
                                                               '\u001F',
                                                               '"',
                                                               '\\',
                                                               '\b',
                                                               '\t',
                                                               '\r',
                                                               '\n',
                                                               '\f' };

  protected static final char [] [] CHARS_MASKED = new char [CHARS_TO_MASK.length] [];
  static
  {
    int i = 0;
    CHARS_MASKED[i++] = "\\u0000".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0001".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0002".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0003".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0004".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0005".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0006".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0007".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u000B".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u000E".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u000F".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0010".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0011".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0012".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0013".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0014".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0015".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0016".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0017".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0018".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u0019".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u001A".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u001B".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u001C".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u001D".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u001E".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\u001F".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\\"".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\\\".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\b".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\t".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\r".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\n".toCharArray (); //$NON-NLS-1$
    CHARS_MASKED[i++] = "\\f".toCharArray (); //$NON-NLS-1$
  }

  private JSONHelper ()
  {}

  private static String _escape (@Nonnull final char [] aInput)
  {
    return new String (StringHelper.replaceMultiple (aInput, CHARS_TO_MASK, CHARS_MASKED));
  }

  @Nullable
  public static String jsonEscape (@Nullable final String sInput)
  {
    if (StringHelper.hasNoText (sInput))
      return sInput;

    final char [] aInput = sInput.toCharArray ();
    if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
      return sInput;
    return _escape (aInput);
  }

  public static void jsonEscape (@Nullable final String sInput, @Nonnull final StringBuilder aSB)
  {
    if (StringHelper.hasText (sInput))
    {
      final char [] aInput = sInput.toCharArray ();
      if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
        aSB.append (sInput);
      else
        aSB.append (_escape (aInput));
    }
  }

  public static void jsonEscape (@Nullable final String sInput,
                                 @Nonnull @WillNotClose final Writer aWriter) throws IOException
  {
    if (StringHelper.hasText (sInput))
    {
      final char [] aInput = sInput.toCharArray ();
      if (!StringHelper.containsAny (aInput, CHARS_TO_MASK))
        aWriter.write (aInput, 0, aInput.length);
      else
        aWriter.append (_escape (aInput));
    }
  }
}
