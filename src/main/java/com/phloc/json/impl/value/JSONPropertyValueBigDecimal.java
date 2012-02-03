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

import java.math.BigDecimal;

import javax.annotation.Nonnull;

import com.phloc.json.IJSONPropertyValue;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link BigDecimal}
 * 
 * @author philip
 */
public class JSONPropertyValueBigDecimal extends AbstractJSONPropertyValue <BigDecimal>
{
  public JSONPropertyValueBigDecimal (final BigDecimal aData)
  {
    super (aData);
  }

  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (getData ().toString ());
  }

  public JSONPropertyValueBigDecimal getClone ()
  {
    return new JSONPropertyValueBigDecimal (getData ());
  }

  /**
   * Tries to create a {@link JSONPropertyValueBigDecimal} from the passed JSON
   * string
   * 
   * @param sJSON
   *        the JSON string to convert
   * @return the resulting object
   */
  @Nonnull
  public static JSONPropertyValueBigDecimal fromJSONString (final String sJSON)
  {
    return new JSONPropertyValueBigDecimal (new BigDecimal (sJSON));
  }
}
