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
package com.phloc.json;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.phloc.commons.ICloneable;

/**
 * High level interface describing an object which can be converted to JSON
 * 
 * @author Boris Gregorcic
 */
public interface IJSON extends ICloneable <IJSON>, Serializable
{
  /**
   * @return the JSON String representation of the object
   */
  @Nonnull
  String getJSONString ();

  /**
   * Generates a JSON String representation of this object, either compact or
   * pretty-printed.<br>
   * <br>
   * <b>NOTE:</b>Do not use pretty print for actual data generation, but only
   * for debug purposes!
   * 
   * @param bAlignAndIndent
   *        whether or not the JSON String should be indented and aligned
   *        (pretty printed)
   * @return the resulting JSON String
   */
  @Nonnull
  String getJSONString (boolean bAlignAndIndent);

  /**
   * This is an internal method appending the resulting JSON String to a already
   * existing passed {@link StringBuilder}. This is more efficient than to
   * create a new {@link StringBuilder} at each level.
   * 
   * @param aResult
   *        The {@link StringBuilder} to append to
   * @param bAlignAndIndent
   *        <code>true</code> if pretty print should be used
   * @param nLevel
   *        Current level of indentation
   */
  void appendJSONString (@Nonnull StringBuilder aResult, boolean bAlignAndIndent, int nLevel);
}
