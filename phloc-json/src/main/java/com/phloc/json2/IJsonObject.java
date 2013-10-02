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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Base interface for a JSON object that is a map from String to IJson
 * 
 * @author Philip Helger
 */
public interface IJsonObject extends IJson, IHasSize, Iterable <Map.Entry <String, IJson>>
{
  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, @Nonnull IJson aValue);

  /**
   * Add at the specified index using the JSON converter
   * 
   * @param sName
   *        The name of the item. May neither be <code>null</code> nor empty.
   * @param aValue
   *        The value to be added. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, @Nullable Object aValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, boolean bValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, byte nValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, int nValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, long nValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, short nValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, float fValue);

  @Nonnull
  IJsonObject add (@Nonnull @Nonempty String sName, double dValue);

  @Nonnull
  EChange removeKey (@Nullable String sName);

  boolean containsKey (@Nullable String sName);

  @Nonnull
  @ReturnsMutableCopy
  Set <String> keySet ();

  @Nonnull
  @ReturnsMutableCopy
  Collection <IJson> values ();

  /**
   * @return A copy of all contained items. Never <code>null</code> but maybe
   *         empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <String, IJson> getAll ();
}
