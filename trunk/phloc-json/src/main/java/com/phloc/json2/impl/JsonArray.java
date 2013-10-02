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
package com.phloc.json2.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json2.IJson;
import com.phloc.json2.IJsonArray;
import com.phloc.json2.convert.JsonConverter;

/**
 * Default implementation of {@link IJsonArray}
 * 
 * @author Philip Helger
 */
public class JsonArray implements IJsonArray
{
  private final List <IJson> m_aValues;

  public JsonArray ()
  {
    this (10);
  }

  public JsonArray (@Nonnegative final int nInitialCapacity)
  {
    m_aValues = new ArrayList <IJson> (nInitialCapacity);
  }

  public boolean isArray ()
  {
    return true;
  }

  public boolean isObject ()
  {
    return false;
  }

  public boolean isValue ()
  {
    return false;
  }

  @Nonnegative
  public int size ()
  {
    return m_aValues.size ();
  }

  public boolean isEmpty ()
  {
    return m_aValues.isEmpty ();
  }

  @Nonnull
  public Iterator <IJson> iterator ()
  {
    return m_aValues.iterator ();
  }

  @Nonnull
  public JsonArray add (@Nonnull final IJson aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aValues.add (aValue);
    return this;
  }

  @Nonnull
  public JsonArray add (@Nullable final Object aValue)
  {
    final IJson aJson = JsonConverter.convertToJson (aValue);
    return add (aJson);
  }

  @Nonnull
  public JsonArray add (final boolean bValue)
  {
    return add (JsonValue.create (bValue));
  }

  @Nonnull
  public JsonArray add (final byte nValue)
  {
    return add (JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (final char cValue)
  {
    return add (JsonValue.create (cValue));
  }

  @Nonnull
  public JsonArray add (final double dValue)
  {
    return add (JsonValue.create (dValue));
  }

  @Nonnull
  public JsonArray add (final float fValue)
  {
    return add (JsonValue.create (fValue));
  }

  @Nonnull
  public JsonArray add (final int nValue)
  {
    return add (JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (final long nValue)
  {
    return add (JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (final short nValue)
  {
    return add (JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, @Nonnull final IJson aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aValues.add (nIndex, aValue);
    return this;
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, @Nullable final Object aValue)
  {
    final IJson aJson = JsonConverter.convertToJson (aValue);
    return add (nIndex, aJson);
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final boolean bValue)
  {
    return add (nIndex, JsonValue.create (bValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final byte nValue)
  {
    return add (nIndex, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final char cValue)
  {
    return add (nIndex, JsonValue.create (cValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final double dValue)
  {
    return add (nIndex, JsonValue.create (dValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final float fValue)
  {
    return add (nIndex, JsonValue.create (fValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final int nValue)
  {
    return add (nIndex, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final long nValue)
  {
    return add (nIndex, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonArray add (@Nonnegative final int nIndex, final short nValue)
  {
    return add (nIndex, JsonValue.create (nValue));
  }

  @Nonnull
  public EChange removeAtIndex (@Nonnegative final int nIndex)
  {
    return ContainerHelper.removeElementAtIndex (m_aValues, nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IJson> getAll ()
  {
    return ContainerHelper.newList (m_aValues);
  }

  @Nonnull
  public JsonArray getClone ()
  {
    final JsonArray ret = new JsonArray (m_aValues.size ());
    for (final IJson aValue : m_aValues)
      ret.add (aValue.getClone ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final JsonArray rhs = (JsonArray) o;
    return m_aValues.equals (rhs.m_aValues);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValues).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("values", m_aValues).toString ();
  }
}
