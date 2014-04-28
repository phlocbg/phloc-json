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

import com.phloc.json2.parser.ParseException;
import com.phloc.json2.parser.Token;

/**
 * Special CSS handler that is invoked during reading in case of a recoverable
 * errors. In case of unrecoverable errors, a {@link ParseException} is thrown!
 * 
 * @author Philip Helger
 */
public interface IJsonParseErrorHandler
{
  /**
   * Called upon a recoverable error. The parameter list is similar to the one
   * of the {@link ParseException}.
   * 
   * @param aLastValidToken
   *        The last valid token. May not be <code>null</code>.
   * @param aExpectedTokenSequencesVal
   *        The expected token. May not be <code>null</code>.
   * @param aTokenImageVal
   *        The error token image. May not be <code>null</code>.
   * @param aLastSkippedToken
   *        The token until which was skipped (incl.) May be <code>null</code>.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   */
  void onJsonParseError (@Nonnull Token aLastValidToken,
                         @Nonnull int [][] aExpectedTokenSequencesVal,
                         @Nonnull String [] aTokenImageVal,
                         @Nullable Token aLastSkippedToken) throws ParseException;
}
