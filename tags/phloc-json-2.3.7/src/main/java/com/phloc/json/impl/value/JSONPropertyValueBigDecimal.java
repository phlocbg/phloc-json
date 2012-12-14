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

import com.phloc.commons.string.StringParser;
import com.phloc.json.IJSONPropertyValue;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link BigDecimal}
 * 
 * @author philip
 */
public class JSONPropertyValueBigDecimal extends AbstractJSONPropertyValueNumeric <BigDecimal>
{
  public JSONPropertyValueBigDecimal (@Nonnull final BigDecimal aData)
  {
    super (aData);
  }

  @Nonnull
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
   * @throws IllegalArgumentException
   *         If the passed string is not null but cannot be converted to a
   *         {@link BigDecimal}
   */
  @Nonnull
  public static JSONPropertyValueBigDecimal fromJSONString (final String sJSON)
  {
    final BigDecimal aValue = StringParser.parseBigDecimal (sJSON);
    if (sJSON != null && aValue == null)
      throw new IllegalArgumentException ("Parsed JSON '" + sJSON + "' is not a BigDecimal!");
    return new JSONPropertyValueBigDecimal (aValue);
  }
}
