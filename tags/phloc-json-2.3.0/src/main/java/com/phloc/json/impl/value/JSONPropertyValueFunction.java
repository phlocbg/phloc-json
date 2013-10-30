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

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueNotParsable;
import com.phloc.json.impl.JSONReader;

/**
 * Implementation of {@link IJSONPropertyValue} with the internal data type
 * {@link String}<br>
 * <br>
 * <b>ATTENTION:</b> function properties cannot be parsed again due to the fact,
 * that the {@link JSONReader#parseObject(String)} uses the default
 * {@link com.fasterxml.jackson.databind.ObjectMapper} which is not capable to
 * handle functions.
 * 
 * @author Boris Gregorcic
 */
public class JSONPropertyValueFunction extends AbstractJSONPropertyValue <String> implements
                                                                                 IJSONPropertyValueNotParsable <String>
{
  private final String m_sBody;
  private final String [] m_aParams;

  @Nonnull
  private static String _getFunctionCode (@Nonnull final String sBody, @Nonnull final String... aParams)
  {
    final StringBuilder aData = new StringBuilder ("function(");
    boolean bFirst = true;
    for (final String sParam : aParams)
    {
      if (!bFirst)
        aData.append (",");
      aData.append (sParam);
      bFirst = false;
    }
    aData.append ("){").append (sBody).append ("}");
    return aData.toString ();
  }

  /**
   * Ctor
   * 
   * @param sBody
   *        The function body (executed code)
   * @param aParams
   *        The parameter arguments this function takes
   */
  public JSONPropertyValueFunction (@Nonnull final String sBody, @Nonnull final String... aParams)
  {
    super (_getFunctionCode (sBody, aParams));
    m_sBody = sBody;
    m_aParams = ArrayHelper.getCopy (aParams);
  }

  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (getData ());
  }

  @Nonnull
  public JSONPropertyValueFunction getClone ()
  {
    return new JSONPropertyValueFunction (m_sBody, m_aParams);
  }
}