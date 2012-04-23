/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.json.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.JSONHelper;

/**
 * Default implementation of {@link IJSONProperty}
 * 
 * @author Boris Gregorcic
 * @param <T>
 */
public final class JSONProperty <T> extends AbstractJSON implements IJSONProperty <T>
{
  private final String m_sName;
  private IJSONPropertyValue <T> m_aValue;

  /**
   * Ctor
   * 
   * @param sName
   * @param aValue
   */
  private JSONProperty (@Nullable final String sName, @Nonnull final IJSONPropertyValue <T> aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");
    m_sName = sName;
    m_aValue = aValue.getClone ();
  }

  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public IJSONPropertyValue <T> getValue ()
  {
    return m_aValue;
  }

  public void setValue (@Nonnull final IJSONPropertyValue <T> aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aValue = aValue.getClone ();
  }

  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    indent (aResult, nLevel, bAlignAndIndent);
    final String sEscaped = JSONHelper.jsonEscape (m_sName);
    if (false && m_sName.equals (sEscaped) && Character.isJavaIdentifierStart (StringHelper.getFirstChar (m_sName)))
      aResult.append (m_sName);
    else
      aResult.append (CJSONConstants.DOUBLEQUOTE).append (sEscaped).append (CJSONConstants.DOUBLEQUOTE);
    aResult.append (CJSONConstants.VALUE_ASSIGNMENT);
    m_aValue.appendJSONString (aResult, bAlignAndIndent, nLevel);
  }

  @Nonnull
  public JSONProperty <T> getClone ()
  {
    return new JSONProperty <T> (m_sName, m_aValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof JSONProperty <?>))
      return false;
    final JSONProperty <?> rhs = (JSONProperty <?>) o;
    return EqualsUtils.equals (m_sName, rhs.m_sName) && m_aValue.equals (rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sName).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("name", m_sName).append ("value", m_aValue).toString ();
  }

  @Nonnull
  public static <U> JSONProperty <U> create (@Nullable final String sName, @Nonnull final IJSONPropertyValue <U> aValue)
  {
    return new JSONProperty <U> (sName, aValue);
  }
}
