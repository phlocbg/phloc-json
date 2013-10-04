/**
 * Copyright (C) 2006-2013 phloc systems
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

import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.JSONHelper;
import com.phloc.json.impl.CJSONConstants;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link String}
 * 
 * @author Boris Gregorcic
 */
public class JSONPropertyValueString extends AbstractJSONPropertyValue <String>
{
  /**
   * Ctor
   * 
   * @param sData
   */
  public JSONPropertyValueString (@Nonnull final String sData)
  {
    super (sData);
  }

  public void appendJSONString (@Nonnull final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (CJSONConstants.DOUBLEQUOTE)
           .append (JSONHelper.jsonEscape (getData ()))
           .append (CJSONConstants.DOUBLEQUOTE);
  }

  @Nonnull
  public JSONPropertyValueString getClone ()
  {
    return new JSONPropertyValueString (getData ());
  }
}
