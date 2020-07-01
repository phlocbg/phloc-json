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
package com.phloc.json.impl;

/**
 * This exception is thrown if compressing or decompressing a JSON String is
 * failing.
 * 
 * @author Boris Gregorcic
 */
public class JSONCompressException extends Exception
{
  private static final long serialVersionUID = -4089436027687807790L;

  /**
   * Ctor with only a message
   * 
   * @param sMsg
   *        Exception message
   */
  public JSONCompressException (final String sMsg)
  {
    super (sMsg);
  }

  /**
   * Ctor with a message and an inner exception
   * 
   * @param sMessage
   *        Exception message
   * @param aException
   *        nested exception
   */
  public JSONCompressException (final String sMessage, final Throwable aException)
  {
    super (sMessage, aException);
  }
}
