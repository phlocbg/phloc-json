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
package com.phloc.json.impl;

import javax.annotation.concurrent.Immutable;

/**
 * @author Boris Gregorcic
 */
@Immutable
public final class CJSONConstants
{
  /**
   * character opening a JSON object
   */
  public static final char OBJECT_START = '{';
  /**
   * character closing a JSON object
   */
  public static final char OBJECT_END = '}';
  /**
   * character opening a JSON list/array
   */
  public static final char LIST_START = '[';
  /**
   * character closing a JSON list/array
   */
  public static final char LIST_END = ']';
  /**
   * character used as separator between JSON properties or objects
   */
  public static final char TOKEN_SEPARATOR = ',';
  /**
   * character used for assignments (between property name and property value)
   */
  public static final char VALUE_ASSIGNMENT = ':';
  /**
   * character used for quoting string property values
   */
  public static final char QUOTE = '\'';
  /**
   * character used for quoting string property values
   */
  public static final char DOUBLEQUOTE = '\"';

  /**
   * JS keyword for null
   */
  public static final String KEYWORD_NULL = "null"; //$NON-NLS-1$

  private CJSONConstants ()
  {
    // private
  }
}
