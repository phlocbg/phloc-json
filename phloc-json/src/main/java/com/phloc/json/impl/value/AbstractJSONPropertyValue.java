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
 *        The internal data type of this property
 */
public abstract class AbstractJSONPropertyValue <DATATYPE> extends AbstractJSON implements IJSONPropertyValue <DATATYPE>
{
  private static final long serialVersionUID = -2938754463345307834L;
  private DATATYPE m_aData;

  protected AbstractJSONPropertyValue ()
  {
    super ();
    this.m_aData = null;// NOPMD
  }

  protected AbstractJSONPropertyValue (@Nonnull final DATATYPE aData)
  {
    super ();
    if (aData == null)
    {
      throw new NullPointerException ("aData"); //$NON-NLS-1$
    }
    this.m_aData = aData;
  }

  @Override
  @Nonnull
  @OverrideOnDemand
  public DATATYPE getData ()
  {
    if (this.m_aData == null)
    {
      throw new IllegalStateException ("getData must be overwritten!"); //$NON-NLS-1$
    }
    return this.m_aData;
  }

  @Override
  @Deprecated
  public final void setData (@Nonnull final DATATYPE aData)
  {
    if (aData == null)
    {
      throw new NullPointerException ("aData"); //$NON-NLS-1$
    }
    this.m_aData = aData;
  }

  @Override
  public boolean equals (final Object aOther)
  {
    if (aOther == this)
    {
      return true;
    }
    if (aOther == null || !getClass ().equals (aOther.getClass ()))
    {
      return false;
    }
    final AbstractJSONPropertyValue <?> rhs = (AbstractJSONPropertyValue <?>) aOther;
    return EqualsUtils.equals (this.m_aData, rhs.m_aData);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_aData).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("data", this.m_aData).toString (); //$NON-NLS-1$
  }
}
