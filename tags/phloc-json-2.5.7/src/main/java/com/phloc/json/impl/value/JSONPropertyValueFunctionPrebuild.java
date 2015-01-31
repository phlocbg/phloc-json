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

import com.phloc.json.IJSONPropertyValueNotParsable;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link String} representing a pre-build function <br>
 * <b>ATTENTION:</b> function properties cannot be parsed again due to the fact,
 * that the {@link JSONReader#parseObject(String)} uses the default
 * {@link com.fasterxml.jackson.databind.ObjectMapper} which is not capable to
 * handle functions.
 * 
 * @author Philip Helger
 */
public class JSONPropertyValueFunctionPrebuild extends AbstractJSONPropertyValue <String> implements IJSONPropertyValueNotParsable <String>
{
  private static final long serialVersionUID = 1861816440879230710L;

  /**
   * Ctor
   * 
   * @param sFunctionCode
   *        The function code done
   */
  public JSONPropertyValueFunctionPrebuild (@Nonnull final String sFunctionCode)
  {
    super (sFunctionCode);
  }

  @Override
  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (getData ());
  }

  @Override
  @Nonnull
  public JSONPropertyValueFunctionPrebuild getClone ()
  {
    return new JSONPropertyValueFunctionPrebuild (getData ());
  }
}
