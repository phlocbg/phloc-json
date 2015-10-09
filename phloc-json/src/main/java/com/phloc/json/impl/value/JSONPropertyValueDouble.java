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

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link Double}
 * 
 * @author Boris Gregorcic
 */
public class JSONPropertyValueDouble extends AbstractJSONPropertyValueNumeric <Double>
{// NOPMD
  private static final long serialVersionUID = 8185684520958800838L;

  public JSONPropertyValueDouble (@Nonnull final Double aData)
  {
    super (aData);
  }

  public JSONPropertyValueDouble (final double dData)
  {
    this (Double.valueOf (dData));
  }

  @Override
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
   * @throws IllegalArgumentException
   *         If the passed string is not null but cannot be converted to a
   *         {@link Double}
   */
  @Nonnull
  public static JSONPropertyValueDouble fromJSONString (final String sJSON)
  {
    final Double aValue = StringParser.parseDoubleObj (sJSON);
    if (sJSON != null && aValue == null)
    {
      throw new IllegalArgumentException ("Parsed JSON '" + sJSON + "' is not a double!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return new JSONPropertyValueDouble (aValue);
  }
}
