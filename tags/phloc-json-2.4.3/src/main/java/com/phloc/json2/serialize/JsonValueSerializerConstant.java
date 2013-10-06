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
package com.phloc.json2.serialize;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json2.IJsonValueSerializer;

/**
 * A {@link IJsonValueSerializer} implementation that uses a constant value.
 * Required for true, false and null.
 * 
 * @author Philip Helger
 */
public final class JsonValueSerializerConstant implements IJsonValueSerializer
{
  private final String m_sValue;

  public JsonValueSerializerConstant (@Nonnull @Nonempty final String sValue)
  {
    if (StringHelper.hasNoText (sValue))
      throw new IllegalArgumentException ("value");
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }

  public void appendAsJsonString (@Nullable final Object aValue, @Nonnull final Writer aWriter) throws IOException
  {
    aWriter.write (m_sValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof JsonValueSerializerConstant))
      return false;
    final JsonValueSerializerConstant rhs = (JsonValueSerializerConstant) o;
    return m_sValue.equals (rhs.m_sValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_sValue).toString ();
  }
}
