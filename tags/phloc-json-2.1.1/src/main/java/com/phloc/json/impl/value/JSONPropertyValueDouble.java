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
package com.phloc.json.impl.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;
import com.phloc.json.IJSONPropertyValue;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link Double}
 * 
 * @author Boris Gregorcic
 */
public class JSONPropertyValueDouble extends AbstractJSONPropertyValue <Double>
{
  public JSONPropertyValueDouble (@Nullable final Double aData)
  {
    super (aData);
  }

  public JSONPropertyValueDouble (final double dData)
  {
    this (Double.valueOf (dData));
  }

  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (getData ().toString ());
  }

  @Nonnull
  public JSONPropertyValueDouble getClone ()
  {
    return new JSONPropertyValueDouble (getData ());
  }

  /**
   * Tries to create a {@link JSONPropertyValueDouble} from the passed JSON
   * string
   * 
   * @param sJSON
   *        the JSON string to convert
   * @return the resulting object
   */
  @Nonnull
  public static JSONPropertyValueDouble fromJSONString (final String sJSON)
  {
    return new JSONPropertyValueDouble (StringHelper.parseDoubleObj (sJSON));
  }
}
