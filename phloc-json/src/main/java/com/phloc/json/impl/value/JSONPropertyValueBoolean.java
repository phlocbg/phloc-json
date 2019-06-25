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

import com.phloc.commons.string.StringParser;
import com.phloc.json.IJSONPropertyValue;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link Boolean}
 *
 * @author Boris Gregorcic
 */
public class JSONPropertyValueBoolean extends AbstractJSONPropertyValue <Boolean>
{
  private static final long serialVersionUID = 2460090172841816466L;

  /**
   * Ctor
   *
   * @param aData
   *        data value
   */
  public JSONPropertyValueBoolean (@Nonnull final Boolean aData)
  {
    super (aData);
  }

  /**
   * Ctor
   *
   * @param bData
   *        data value
   */
  public JSONPropertyValueBoolean (final boolean bData)
  {
    this (Boolean.valueOf (bData));
  }

  @Override
  public void appendJSONString (@Nonnull final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (getData ().toString ());
  }

  @Override
  @Nonnull
  public JSONPropertyValueBoolean getClone ()
  {
    return new JSONPropertyValueBoolean (getData ());
  }

  /**
   * Tries to create a {@link JSONPropertyValueBoolean} from the passed JSON
   * string
   *
   * @param sJSON
   *        the JSON string to convert
   * @return the resulting object
   */
  @Nonnull
  public static JSONPropertyValueBoolean fromJSONString (final String sJSON)
  {
    return new JSONPropertyValueBoolean (StringParser.parseBool (sJSON));
  }
}
