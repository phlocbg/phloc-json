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
package com.phloc.json.impl;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json2.serialize.JsonHelper;

/**
 * Default implementation of {@link IJSONProperty}
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 */
public final class JSONProperty <DATATYPE> extends AbstractJSON implements IJSONProperty <DATATYPE>
{
  private final String m_sName;
  private IJSONPropertyValue <DATATYPE> m_aValue;

  /**
   * Ctor
   * 
   * @param sName
   *        property name. May neither be <code>null</code> nor empty.
   * @param aValue
   *        Property value. May not be <code>null</code>.
   */
  private JSONProperty (@Nonnull @Nonempty final String sName, @Nonnull final IJSONPropertyValue <DATATYPE> aValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name may not be empty");
    if (aValue == null)
      throw new NullPointerException ("value");
    m_sName = sName;
    m_aValue = aValue.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public IJSONPropertyValue <DATATYPE> getValue ()
  {
    return m_aValue;
  }

  public void setValue (@Nonnull final IJSONPropertyValue <DATATYPE> aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aValue = aValue.getClone ();
  }

  public void appendJSONString (@Nonnull final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.DOUBLEQUOTE)
           .append (JsonHelper.jsonEscape (m_sName))
           .append (CJSONConstants.DOUBLEQUOTE)
           .append (CJSONConstants.VALUE_ASSIGNMENT);
    m_aValue.appendJSONString (aResult, bAlignAndIndent, nLevel);
  }

  @Nonnull
  public JSONProperty <DATATYPE> getClone ()
  {
    return new JSONProperty <DATATYPE> (m_sName, m_aValue);
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
  public static <DATATYPE> JSONProperty <DATATYPE> create (@Nonnull @Nonempty final String sName,
                                                           @Nonnull final IJSONPropertyValue <DATATYPE> aValue)
  {
    return new JSONProperty <DATATYPE> (sName, aValue);
  }
}
