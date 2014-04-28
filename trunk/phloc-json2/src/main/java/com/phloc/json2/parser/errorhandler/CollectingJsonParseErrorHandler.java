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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json2.parser.ParseException;
import com.phloc.json2.parser.Token;

/**
 * A collecting implementation of {@link IJsonParseErrorHandler}. So in case a
 * recoverable error occurs, it is remembered in the internal list and can be
 * retrieved by {@link #getAllParseErrors()}.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class CollectingJsonParseErrorHandler implements IJsonParseErrorHandler
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private final List <JsonParseError> m_aErrors = new ArrayList <JsonParseError> ();
  private final IJsonParseErrorHandler m_aNestedErrorHandler;

  public CollectingJsonParseErrorHandler ()
  {
    this (null);
  }

  public CollectingJsonParseErrorHandler (@Nullable final IJsonParseErrorHandler aNestedErrorHandler)
  {
    m_aNestedErrorHandler = aNestedErrorHandler;
  }

  public void onJsonParseError (@Nonnull final Token aLastValidToken,
                                @Nonnull final int [][] aExpectedTokenSequencesVal,
                                @Nonnull final String [] aTokenImageVal,
                                @Nullable final Token aLastSkippedToken) throws ParseException
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aErrors.add (new JsonParseError (aLastValidToken, aExpectedTokenSequencesVal, aTokenImageVal, aLastSkippedToken));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    if (m_aNestedErrorHandler != null)
      m_aNestedErrorHandler.onJsonParseError (aLastValidToken,
                                              aExpectedTokenSequencesVal,
                                              aTokenImageVal,
                                              aLastSkippedToken);
  }

  /**
   * @return <code>true</code> if at least one parse error is contained,
   *         <code>false</code> otherwise.
   */
  @Nonnegative
  public boolean hasParseErrors ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return !m_aErrors.isEmpty ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The number of contained parse errors. Always &ge; 0.
   */
  @Nonnegative
  public int getParseErrorCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aErrors.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A copy of the list with all contained errors. Never
   *         <code>null</code> but maybe empty.
   * @see #getParseErrorCount()
   * @see #hasParseErrors()
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <JsonParseError> getAllParseErrors ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aErrors);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("errors", m_aErrors).toString ();
  }
}
