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
package com.phloc.json;

import javax.annotation.Nonnull;

/**
 * Interface for a JSON property value. The internal data type can be specified
 * via the template parameter
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        the internal data type of the value
 */
public interface IJSONPropertyValue <DATATYPE> extends IJSON
{
  /**
   * @return the internal data value
   */
  @Nonnull
  DATATYPE getData ();

  /**
   * Sets the passed data value
   * 
   * @param aValue
   *        the value to set
   */
  @Deprecated
  void setData (@Nonnull DATATYPE aValue);

  /**
   * {@inheritDoc}
   */
  @Nonnull
  IJSONPropertyValue <DATATYPE> getClone ();
}
