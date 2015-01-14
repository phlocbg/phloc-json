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
import javax.annotation.Nullable;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.json.IJSONPropertyValueNotParsable;

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
public class JSONPropertyValueFunction extends AbstractJSONPropertyValue <String> implements IJSONPropertyValueNotParsable <String>
{
  private static final long serialVersionUID = 3114832342147725869L;
  private final String m_sBody;
  private final String [] m_aParams;

  @Nonnull
  private static String getFunctionCode (@Nonnull final String sBody, @Nullable final String [] aParams)
  {
    final StringBuilder aData = new StringBuilder ("function("); //$NON-NLS-1$
    boolean bFirst = true;
    if (aParams != null)
    {
      for (final String sParam : aParams)
      {
        if (bFirst)
        {
          bFirst = false;
        }
        else
        {
          aData.append (',');
        }
        aData.append (sParam);
      }
    }
    aData.append ("){").append (sBody).append ('}'); //$NON-NLS-1$
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
  public JSONPropertyValueFunction (@Nonnull final String sBody, @Nullable final String... aParams)
  {
    super (getFunctionCode (sBody, aParams));
    this.m_sBody = sBody;
    this.m_aParams = ArrayHelper.getCopy (aParams);
  }

  @Override
  public void appendJSONString (@Nonnull final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    aResult.append (getData ());
  }

  @Override
  @Nonnull
  public JSONPropertyValueFunction getClone ()
  {
    return new JSONPropertyValueFunction (this.m_sBody, this.m_aParams);
  }
}
