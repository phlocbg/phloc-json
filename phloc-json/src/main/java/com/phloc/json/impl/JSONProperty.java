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
package com.phloc.json.impl;

import java.util.Set;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.JSONHelper;

/**
 * Default implementation of {@link IJSONProperty}
 *
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        The internal data type of this property
 */
public final class JSONProperty <DATATYPE> extends AbstractJSON implements IJSONProperty <DATATYPE>
{
  private static final long serialVersionUID = 5022572758866668024L;
  private final String m_sName;
  private IJSONPropertyValue <DATATYPE> m_aValue;

  /**
   * Ctor
   *
   * @param sName
   *        property name. May neither be <code>null</code> nor empty.
   * @param aValue
   *        Property value. May not be <code>null</code>.
   * @param eCloneStrategy
   *        How to deal with the value regarding cloning
   */
  private JSONProperty (@Nonnull @Nonempty final String sName,
                        @Nonnull final IJSONPropertyValue <DATATYPE> aValue,
                        final ECloneStategy eCloneStrategy)
  {
    super ();
    if (StringHelper.hasNoText (sName))
    {
      throw new IllegalArgumentException ("sName must not be null or empty!"); //$NON-NLS-1$
    }
    if (aValue == null)
    {
      throw new NullPointerException ("aValue"); //$NON-NLS-1$
    }
    this.m_sName = sName;
    this.m_aValue = eCloneStrategy == ECloneStategy.FORCE ||
                    eCloneStrategy != ECloneStategy.AVOID && JSONSettings.getInstance ().isCloneProperties ()
                                                                                                              ? aValue.getClone ()
                                                                                                              : aValue;
    JSONStatistics.getInstance ().onPropertyCreated ();
  }

  @Override
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return this.m_sName;
  }

  @Override
  @Nonnull
  public IJSONPropertyValue <DATATYPE> getValue ()
  {
    return this.m_aValue;
  }

  @Override
  public void setValue (@Nonnull final IJSONPropertyValue <DATATYPE> aValue)
  {
    setValue (aValue, ECloneStategy.INHERIT);
  }

  private void setValue (@Nonnull final IJSONPropertyValue <DATATYPE> aValue, final ECloneStategy eCloneStrategy)
  {
    if (aValue == null)
    {
      throw new NullPointerException ("aValue"); //$NON-NLS-1$
    }
    this.m_aValue = eCloneStrategy == ECloneStategy.FORCE ||
                    eCloneStrategy != ECloneStategy.AVOID && JSONSettings.getInstance ().isCloneProperties ()
                                                                                                              ? aValue.getClone ()
                                                                                                              : aValue;
  }

  @Override
  public void appendJSONString (@Nonnull final StringBuilder aResult,
                                final boolean bAlignAndIndent,
                                final int nLevel,
                                final Set <IJSONObject> aAncestors)
  {
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.DOUBLEQUOTE)
           .append (JSONHelper.jsonEscape (this.m_sName))
           .append (CJSONConstants.DOUBLEQUOTE)
           .append (CJSONConstants.VALUE_ASSIGNMENT);
    this.m_aValue.appendJSONString (aResult, bAlignAndIndent, nLevel, aAncestors);
  }

  @Override
  @Nonnull
  public JSONProperty <DATATYPE> getClone ()
  {
    return new JSONProperty <DATATYPE> (this.m_sName, this.m_aValue, ECloneStategy.FORCE);
  }

  @Override
  public boolean equals (final Object aOther)
  {
    if (aOther == this)
    {
      return true;
    }
    if (!(aOther instanceof JSONProperty <?>))
    {
      return false;
    }
    final JSONProperty <?> rhs = (JSONProperty <?>) aOther;
    return EqualsUtils.equals (this.m_sName, rhs.m_sName) && this.m_aValue.equals (rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_sName).append (this.m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("name", this.m_sName).append ("value", this.m_aValue).toString (); //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Nonnull
  public static <DATATYPE> JSONProperty <DATATYPE> create (@Nonnull @Nonempty final String sName,
                                                           @Nonnull final IJSONPropertyValue <DATATYPE> aValue,
                                                           final ECloneStategy eCloneStrategy)
  {
    return new JSONProperty <DATATYPE> (sName, aValue, eCloneStrategy);
  }
}
