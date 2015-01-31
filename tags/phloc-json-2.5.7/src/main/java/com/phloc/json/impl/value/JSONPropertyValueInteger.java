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
package com.phloc.json.impl.value;

import javax.annotation.Nonnull;

import com.phloc.commons.string.StringParser;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link Integer}
 * 
 * @author Boris Gregorcic
 */
public class JSONPropertyValueInteger extends AbstractJSONPropertyValueNumeric <Integer>
{// NOPMD
  private static final long serialVersionUID = 3460792306207775710L;

  public JSONPropertyValueInteger (@Nonnull final Integer aData)
  {
    super (aData);
  }

  public JSONPropertyValueInteger (final int nData)
  {
    this (Integer.valueOf (nData));
  }

  @Override
  @Nonnull
  public JSONPropertyValueInteger getClone ()
  {
    return new JSONPropertyValueInteger (getData ());
  }

  /**
   * Tries to create a {@link JSONPropertyValueInteger} from the passed JSON
   * string
   * 
   * @param sJSON
   *        the JSON string to convert
   * @return the resulting object
   * @throws IllegalArgumentException
   *         If the passed string is not null but cannot be converted to an
   *         {@link Integer}
   */
  @Nonnull
  public static JSONPropertyValueInteger fromJSONString (final String sJSON)
  {
    final Integer aValue = StringParser.parseIntObj (sJSON);
    if (sJSON != null && aValue == null)
    {
      throw new IllegalArgumentException ("Parsed JSON '" + sJSON + "' is not an integer!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return new JSONPropertyValueInteger (aValue);
  }
}
