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
package com.phloc.json2;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of {@link IJsonValue}.
 * 
 * @author Philip Helger
 */
public class JsonValue implements IJsonValue
{
  /** Special value for "true" */
  public static final IJsonValue TRUE = new JsonValue (Boolean.TRUE);
  /** Special value for "false" */
  public static final IJsonValue FALSE = new JsonValue (Boolean.FALSE);
  /** Special value for "null" */
  public static final IJsonValue NULL = new JsonValue (null);

  /** Cache for regular used numeric JSON values */
  private static final int INT_CACHE_MIN = -128;
  private static final int INT_CACHE_MAX = 127;
  private static final IJsonValue [] NUMERIC = new IJsonValue [INT_CACHE_MAX - INT_CACHE_MIN + 1];

  static
  {
    for (int i = INT_CACHE_MIN; i <= INT_CACHE_MAX; ++i)
      NUMERIC[i - INT_CACHE_MIN] = new JsonValue (Integer.valueOf (i));
  }

  private final Serializable m_aValue;

  private JsonValue (@Nullable final Serializable aValue)
  {
    m_aValue = aValue;
  }

  public boolean isArray ()
  {
    return false;
  }

  public boolean isObject ()
  {
    return false;
  }

  public boolean isValue ()
  {
    return true;
  }

  @Nullable
  public Serializable getValue ()
  {
    return m_aValue;
  }

  @Nullable
  public Class <?> getValueClass ()
  {
    return m_aValue == null ? null : m_aValue.getClass ();
  }

  public boolean isNullValue ()
  {
    return m_aValue == null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue).toString ();
  }

  @Nonnull
  public static IJsonValue create (final boolean bValue)
  {
    return bValue ? TRUE : FALSE;
  }

  @Nonnull
  public static IJsonValue create (final byte nValue)
  {
    return create ((int) nValue);
  }

  @Nonnull
  public static IJsonValue create (final int nValue)
  {
    // Use cached value
    if (nValue >= 0 && nValue < NUMERIC.length)
      return NUMERIC[nValue];

    return new JsonValue (Integer.valueOf (nValue));
  }

  @Nonnull
  public static IJsonValue create (final long nValue)
  {
    // Use cached value
    if (nValue >= 0 && nValue < NUMERIC.length)
      return NUMERIC[(int) nValue];

    return new JsonValue (Long.valueOf (nValue));
  }

  @Nonnull
  public static IJsonValue create (final short nValue)
  {
    return create ((int) nValue);
  }

  @Nonnull
  public static IJsonValue create (final float fValue)
  {
    return new JsonValue (Float.valueOf (fValue));
  }

  @Nonnull
  public static IJsonValue create (final double dValue)
  {
    return new JsonValue (Double.valueOf (dValue));
  }

  @Nonnull
  public static IJsonValue create (@Nullable final Serializable aValue)
  {
    // Special null constant
    if (aValue == null)
      return NULL;

    // New object
    return new JsonValue (aValue);
  }
}
