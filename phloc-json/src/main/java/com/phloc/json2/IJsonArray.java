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
package com.phloc.json2;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * JSON array.
 * 
 * @author Philip Helger
 */
public interface IJsonArray extends IJsonCollection, Iterable <IJson>
{
  @Nonnull
  IJsonArray add (@Nonnull IJson aValue);

  /**
   * Add using the JSON converter
   * 
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  IJsonArray add (@Nullable Object aValue);

  @Nonnull
  IJsonArray add (boolean bValue);

  @Nonnull
  IJsonArray add (byte nValue);

  @Nonnull
  IJsonArray add (char cValue);

  @Nonnull
  IJsonArray add (double dValue);

  @Nonnull
  IJsonArray add (float fValue);

  @Nonnull
  IJsonArray add (int nValue);

  @Nonnull
  IJsonArray add (long nValue);

  @Nonnull
  IJsonArray add (short nValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, @Nonnull IJson aValue);

  /**
   * Add at the specified index using the JSON converter
   * 
   * @param nIndex
   *        The index where the item should be added. Must be &ge; 0.
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, @Nullable Object aValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, boolean bValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, byte nValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, char fValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, double dValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, float fValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, int nValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, long nValue);

  @Nonnull
  IJsonArray add (@Nonnegative int nIndex, short nValue);

  @Nonnull
  EChange removeAtIndex (@Nonnegative int nIndex);

  /**
   * Get the element at the specified index.
   * 
   * @param nIndex
   *        The index to retrieve.
   * @return <code>null</code> if the index is invalid.
   */
  @Nullable
  IJson getAtIndex (@Nonnegative int nIndex);

  /**
   * Get the element at the specified index. This is the {@link IJsonValue}
   * specific version of {@link #getAtIndex(int)}.
   * 
   * @param nIndex
   *        The index to retrieve.
   * @return <code>null</code> if the index is invalid or if the value is not a
   *         {@link IJsonValue}.
   */
  @Nullable
  IJsonValue getValueAtIndex (@Nonnegative int nIndex);

  /**
   * Get the element at the specified index. This is the {@link IJsonArray}
   * specific version of {@link #getAtIndex(int)}.
   * 
   * @param nIndex
   *        The index to retrieve.
   * @return <code>null</code> if the index is invalid or if the value is not a
   *         {@link IJsonArray}.
   */
  @Nullable
  IJsonArray getArrayAtIndex (@Nonnegative int nIndex);

  /**
   * Get the element at the specified index. This is the {@link IJsonObject}
   * specific version of {@link #getAtIndex(int)}.
   * 
   * @param nIndex
   *        The index to retrieve.
   * @return <code>null</code> if the index is invalid or if the value is not a
   *         {@link IJsonObject}.
   */
  @Nullable
  IJsonObject getObjectAtIndex (@Nonnegative int nIndex);

  /**
   * @return A copy of all contained items. Never <code>null</code> but maybe
   *         empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IJson> getAll ();

  @Nonnull
  IJsonArray getClone ();
}
