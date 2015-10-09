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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.json2.serialize.JsonHelper;

/**
 * Some utility methods for JSON
 * 
 * @author Philip Helger
 */
@Immutable
@Deprecated
public final class JSONHelper
{
  private JSONHelper ()
  {}

  @Nullable
  public static String jsonEscape (@Nullable final String sInput)
  {
    return JsonHelper.jsonEscape (sInput);
  }
}
