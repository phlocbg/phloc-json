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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.impl.CJSONConstants;

/**
 * Default implementation of {@link IJSONPropertyValueList}
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        The inner data type
 */
public class JSONPropertyValueList <DATATYPE extends IJSONPropertyValue <?>> extends AbstractJSONPropertyValue <List <DATATYPE>> implements IJSONPropertyValueList <DATATYPE>
{
  private static final long serialVersionUID = 6549899874312269115L;

  /**
   * Default ctor
   */
  public JSONPropertyValueList ()
  {
    super (new ArrayList <DATATYPE> ());
  }

  /**
   * Ctor
   * 
   * @param aList
   *        List to use as the basis
   */
  public JSONPropertyValueList (@Nullable final Iterable <? extends DATATYPE> aList)
  {
    super (ContainerHelper.newList (aList));
  }

  @Override
  @Nonnull
  public JSONPropertyValueList <DATATYPE> addValue (@Nonnull final DATATYPE aValue)
  {
    if (aValue == null)
    {
      throw new NullPointerException ("value"); //$NON-NLS-1$
    }
    getData ().add (aValue);
    return this;
  }

  @Override
  @Nonnull
  public JSONPropertyValueList <DATATYPE> addAllValues (@Nonnull final Collection <? extends DATATYPE> aValues)
  {
    if (aValues == null)
    {
      throw new NullPointerException ("values"); //$NON-NLS-1$
    }
    getData ().addAll (aValues);
    return this;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <DATATYPE> getValues ()
  {
    return ContainerHelper.newList (getData ());
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <Object> getDataValues ()
  {
    final List <Object> aDataValues = new ArrayList <Object> ();
    for (final DATATYPE aValue : getData ())
    {
      aDataValues.add (aValue.getData ());
    }
    return aDataValues;
  }

  @Override
  @SuppressWarnings ("deprecation")
  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    appendNewLine (aResult, bAlignAndIndent);
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.LIST_START);
    appendNewLine (aResult, bAlignAndIndent);

    int nIndex = 0;
    final List <DATATYPE> aData = getData ();
    final int nElementCount = aData.size ();
    for (final DATATYPE aValue : aData)
    {
      if (!(aValue instanceof IJSONPropertyValueList) &&
          !(aValue instanceof IJSONObject) &&
          !(aValue instanceof JSONPropertyValueJSONObject))
      {
        indent (aResult, nLevel + 1, bAlignAndIndent);
      }
      if (aValue instanceof IJSONObject || aValue instanceof JSONPropertyValueJSONObject)
      {
        // JSON object will increase the level already when passing through
        // PropertyValue method
        aValue.appendJSONString (aResult, bAlignAndIndent, nLevel);
      }
      else
      {
        aValue.appendJSONString (aResult, bAlignAndIndent, nLevel + 1);
      }

      if (nIndex < nElementCount - 1)
        aResult.append (CJSONConstants.TOKEN_SEPARATOR);
      appendNewLine (aResult, bAlignAndIndent);
      nIndex++;
    }
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.LIST_END);
  }

  @Override
  @Nonnull
  public JSONPropertyValueList <DATATYPE> getClone ()
  {
    return new JSONPropertyValueList <DATATYPE> (getData ());
  }
}
