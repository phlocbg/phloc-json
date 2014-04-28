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
package com.phloc.json2.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.json2.IJsonValue;
import com.phloc.json2.IJsonValueSerializer;
import com.phloc.json2.parser.JsonReader;
import com.phloc.json2.serialize.JsonValueSerializerConstant;
import com.phloc.json2.serialize.JsonValueSerializerEscaped;
import com.phloc.json2.serialize.JsonValueSerializerToString;
import com.phloc.json2.serialize.JsonWriter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Default implementation of {@link IJsonValue}.
 * 
 * @author Philip Helger
 */
@Immutable
public class JsonValue implements IJsonValue
{
  /** Special value for "true" */
  public static final JsonValue TRUE = new JsonValue (Boolean.TRUE, new JsonValueSerializerConstant ("true"));
  /** Special value for "false" */
  public static final JsonValue FALSE = new JsonValue (Boolean.FALSE, new JsonValueSerializerConstant ("false"));
  /** Special value for "null" */
  public static final JsonValue NULL = new JsonValue (null, new JsonValueSerializerConstant ("null"));

  /** Cache for regular used numeric JSON values */
  private static final int INT_CACHE_MIN = -128;
  private static final int INT_CACHE_MAX = 127;
  private static final JsonValue [] NUMERIC = new JsonValue [INT_CACHE_MAX - INT_CACHE_MIN + 1];

  static
  {
    for (int i = INT_CACHE_MIN; i <= INT_CACHE_MAX; ++i)
      NUMERIC[i - INT_CACHE_MIN] = new JsonValue (Integer.valueOf (i), JsonValueSerializerToString.getInstance ());
  }

  private Object m_aValue;
  private IJsonValueSerializer m_aValueSerializer;

  private JsonValue (@Nullable final Object aValue, @Nonnull final IJsonValueSerializer aValueSerializer)
  {
    m_aValue = aValue;
    m_aValueSerializer = ValueEnforcer.notNull (aValueSerializer, "ValueSerializer");
  }

  private void writeObject (@Nonnull final ObjectOutputStream aOOS) throws IOException
  {
    final NonBlockingStringWriter aWriter = new NonBlockingStringWriter ();
    m_aValueSerializer.appendAsJsonString (m_aValue, aWriter);
    aOOS.writeUTF (aWriter.getAsString ());
  }

  private void readObject (@Nonnull final ObjectInputStream aOIS) throws IOException
  {
    final String sJson = aOIS.readUTF ();
    final JsonValue aJson = (JsonValue) JsonReader.readFromString (sJson);
    m_aValue = aJson.m_aValue;
    m_aValueSerializer = aJson.m_aValueSerializer;
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
  public Object getValue ()
  {
    return m_aValue;
  }

  @Nullable
  public <T> T getCastedValue (@Nonnull final Class <T> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    return aClass.cast (m_aValue);
  }

  @Nullable
  public <T> T getConvertedValue (@Nonnull final Class <T> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    return TypeConverter.convertIfNecessary (m_aValue, aClass);
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

  @Nonnull
  public IJsonValueSerializer getValueSerializer ()
  {
    return m_aValueSerializer;
  }

  public void appendAsJsonString (@Nonnull final Writer aWriter) throws IOException
  {
    m_aValueSerializer.appendAsJsonString (m_aValue, aWriter);
  }

  @Nonnull
  public JsonValue getClone ()
  {
    // No need to clone, as this object is immutable!
    return this;
  }

  @Nonnull
  public String getAsString ()
  {
    return JsonWriter.getAsString (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final JsonValue rhs = (JsonValue) o;
    return EqualsUtils.equals (m_aValue, rhs.m_aValue) && m_aValueSerializer.equals (rhs.m_aValueSerializer);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).append (m_aValueSerializer).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue)
                                       .append ("valueSerializer", m_aValueSerializer)
                                       .toString ();
  }

  @Nonnull
  public static JsonValue create (final boolean bValue)
  {
    return bValue ? TRUE : FALSE;
  }

  @Nonnull
  public static JsonValue create (final byte nValue)
  {
    return create ((int) nValue);
  }

  @Nonnull
  public static JsonValue create (final char cValue)
  {
    return create (Character.toString (cValue), JsonValueSerializerEscaped.getInstance ());
  }

  @Nonnull
  public static JsonValue create (final double dValue)
  {
    return create (BigDecimal.valueOf (dValue));
  }

  @Nonnull
  public static JsonValue create (final float fValue)
  {
    return create (BigDecimal.valueOf (fValue));
  }

  @Nonnull
  public static JsonValue create (@Nonnull final BigDecimal aValue)
  {
    return create (aValue, JsonValueSerializerToString.getInstance ());
  }

  @Nonnull
  public static JsonValue create (final int nValue)
  {
    // Use cached value
    if (nValue >= INT_CACHE_MIN && nValue < INT_CACHE_MAX)
      return NUMERIC[nValue - INT_CACHE_MIN];

    return create (Integer.valueOf (nValue), JsonValueSerializerToString.getInstance ());
  }

  @Nonnull
  public static JsonValue create (final long nValue)
  {
    // Use cached value
    if (nValue >= INT_CACHE_MIN && nValue < INT_CACHE_MAX)
      return NUMERIC[(int) nValue - INT_CACHE_MIN];

    return create (Long.valueOf (nValue), JsonValueSerializerToString.getInstance ());
  }

  @Nonnull
  public static JsonValue create (final short nValue)
  {
    return create ((int) nValue);
  }

  @Nonnull
  public static JsonValue create (@Nonnull final BigInteger aValue)
  {
    return create (aValue, JsonValueSerializerToString.getInstance ());
  }

  @Nonnull
  @SuppressFBWarnings ("RC_REF_COMPARISON_BAD_PRACTICE_BOOLEAN")
  public static JsonValue create (@Nullable final Object aValue, @Nonnull final IJsonValueSerializer aValueSerializer)
  {
    // Special null constant
    if (aValue == null)
      return NULL;
    // Special true/false
    if (aValue == Boolean.TRUE)
      return TRUE;
    if (aValue == Boolean.FALSE)
      return FALSE;

    // New object
    return new JsonValue (aValue, aValueSerializer);
  }
}
