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

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link Object}
 * 
 * @author Boris Gregorcic
 */
@Deprecated
@DevelopersNote ("Used JSONObject directly!")
public class JSONPropertyValueJSONObject extends AbstractJSONPropertyValue <IJSONObject>
{
  /**
   * Ctor
   * 
   * @param aData
   */
  public JSONPropertyValueJSONObject (@Nonnull final IJSONObject aData)
  {
    super (aData.getClone ());
  }

  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    getData ().appendJSONString (aResult, bAlignAndIndent, nLevel + 1);
  }

  @Nonnull
  public JSONPropertyValueJSONObject getClone ()
  {
    return new JSONPropertyValueJSONObject (getData ());
  }
}
