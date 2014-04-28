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
package com.phloc.json2.parser.errorhandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json2.parser.ParseException;
import com.phloc.json2.parser.Token;

/**
 * An implementation of {@link IJsonParseErrorHandler} that throws a
 * {@link ParseException}. So in case a recoverable error occurs, a new
 * {@link ParseException} is thrown. This is the most strict implementation of
 * {@link IJsonParseErrorHandler}.
 * 
 * @author Philip Helger
 */
public class ThrowingJsonParseErrorHandler implements IJsonParseErrorHandler
{
  private static final ThrowingJsonParseErrorHandler s_aInstance = new ThrowingJsonParseErrorHandler ();

  private ThrowingJsonParseErrorHandler ()
  {}

  /**
   * @return The singleton instance. Never <code>null</code>.
   */
  @Nonnull
  public static ThrowingJsonParseErrorHandler getInstance ()
  {
    return s_aInstance;
  }

  public void onJsonParseError (@Nonnull final Token aLastValidToken,
                                @Nonnull final int [][] aExpectedTokenSequencesVal,
                                @Nonnull final String [] aTokenImageVal,
                                @Nullable final Token aLastSkippedToken) throws ParseException
  {
    throw new ParseException (aLastValidToken, aExpectedTokenSequencesVal, aTokenImageVal);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
