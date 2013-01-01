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
package com.phloc.json.impl;

import com.phloc.commons.CGlobal;
import com.phloc.commons.string.StringHelper;
import com.phloc.json.IJSON;

/**
 * This is the base class for all JSON implementation domain objects. It
 * contains default implementation for shared methods.
 * 
 * @author Boris Gregorcic
 */
public abstract class AbstractJSON implements IJSON
{
  protected static final String INDENT_STRING = "  ";

  public final String getJSONString ()
  {
    return getJSONString (false);
  }

  public final String getJSONString (final boolean bAlignAndIndent)
  {
    final StringBuilder aResult = new StringBuilder ();
    appendJSONString (aResult, bAlignAndIndent, 0);
    return aResult.toString ();
  }

  /**
   * Emits the indentation characters for the current level
   * 
   * @param aResult
   *        The string builder to which to add
   * @param nLevel
   *        the current level in the hierarchy
   * @param bAlignAndIndent
   *        whether or not the output should be indented and aligned at all
   */
  protected static final void indent (final StringBuilder aResult, final int nLevel, final boolean bAlignAndIndent)
  {
    if (bAlignAndIndent)
      aResult.append (StringHelper.getRepeated (INDENT_STRING, nLevel));
  }

  /**
   * Appends a new line
   * 
   * @param aSB
   *        The string builder to which to add
   * @param bAlignAndIndent
   *        whether or not the output should be indented and aligned at all
   */
  protected static final void appendNewLine (final StringBuilder aSB, final boolean bAlignAndIndent)
  {
    // this is to avoid empty lines (is hard to grant otherwise!)
    if (bAlignAndIndent && !StringHelper.endsWith (aSB, CGlobal.LINE_SEPARATOR))
      aSB.append (CGlobal.LINE_SEPARATOR);
  }
}
