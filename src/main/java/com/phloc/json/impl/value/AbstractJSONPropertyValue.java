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
package com.phloc.json.impl.value;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.impl.AbstractJSON;

/**
 * @author Boris Gregorcic
 * @param <DATATYPE>
 */
public abstract class AbstractJSONPropertyValue <DATATYPE> extends AbstractJSON implements
                                                                               IJSONPropertyValue <DATATYPE>
{
  private DATATYPE m_aData;

  protected AbstractJSONPropertyValue ()
  {
    m_aData = null;
  }

  protected AbstractJSONPropertyValue (@Nonnull final DATATYPE aData)
  {
    if (aData == null)
      throw new NullPointerException ("data");
    m_aData = aData;
  }

  @Nonnull
  @OverrideOnDemand
  public DATATYPE getData ()
  {
    if (m_aData == null)
      throw new IllegalStateException ("getData must be overwritten!");
    return m_aData;
  }

  @Deprecated
  public final void setData (@Nonnull final DATATYPE aData)
  {
    if (aData == null)
      throw new NullPointerException ("data");
    m_aData = aData;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractJSONPropertyValue <?> rhs = (AbstractJSONPropertyValue <?>) o;
    return EqualsUtils.equals (m_aData, rhs.m_aData);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aData).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("data", m_aData).toString ();
  }
}
