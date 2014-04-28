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
package com.phloc.json2.parser;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

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

  public static String unescapeString (@Nonnull final StringBuilder aSB)
  {
    final String sWithoutQuotes = aSB.substring (1, aSB.length () - 1);
    return sWithoutQuotes;
  }
}
