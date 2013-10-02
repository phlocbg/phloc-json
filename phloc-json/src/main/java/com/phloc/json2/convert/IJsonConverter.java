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
package com.phloc.json2.convert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.json2.IJson;

/**
 * Single converter from and to {@link IJson}.
 * 
 * @author Philip Helger
 */
public interface IJsonConverter
{
  /**
   * Convert the passed value to JSON
   * 
   * @param aValue
   *        The non-<code>null</code> object to be converted to a JSON object
   * @return Never <code>null</code>.
   */
  @Nonnull
  IJson convertToJson (@Nonnull Object aValue);

  /**
   * Convert the passed object to a native element.
   * 
   * @param aJson
   *        The JSON object to be converted.
   * @return <code>null</code> if conversion to a native object failed.
   */
  @Nullable
  Object convertToNative (@Nonnull IJson aJson);
}
