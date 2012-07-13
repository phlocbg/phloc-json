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
package com.phloc.json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.json.impl.JSONObject;

/**
 * Interface describing a JSON object property. Properties can again be JSON
 * objects and therefore be used to build object hierarchies.<br>
 * A {@link IJSONProperty} consists of the following two parts:<br>
 * <ul>
 * <li>name: {@link String}</li>
 * <li>value: {@link IJSONPropertyValue}</li>
 * </ul>
 * 
 * @author Boris Gregorcic
 */
@DevelopersNote ("Does not implement IHasName because the non-null contract cannot be fulfilled!")
public interface IJSONProperty <T> extends IJSON
{
  /**
   * @return the name of the property (unique in the scope of a
   *         {@link JSONObject})
   */
  @Nullable
  String getName ();

  /**
   * @return the current value ({@link IJSONPropertyValue}), may not be
   *         <code>null</code>
   */
  @Nonnull
  IJSONPropertyValue <T> getValue ();

  /**
   * Sets the passed value in this property
   * 
   * @param aValue
   */
  void setValue (@Nonnull IJSONPropertyValue <T> aValue);

  @Nonnull
  IJSONProperty <T> getClone ();
}
