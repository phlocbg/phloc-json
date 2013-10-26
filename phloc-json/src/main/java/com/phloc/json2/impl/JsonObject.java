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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.impl.JSONParsingException;
import com.phloc.json2.IJson;
import com.phloc.json2.IJsonArray;
import com.phloc.json2.IJsonObject;
import com.phloc.json2.IJsonValue;
import com.phloc.json2.convert.JsonConverter;
import com.phloc.json2.serialize.JsonReader;
import com.phloc.json2.serialize.JsonWriter;

/**
 * Default implementation of {@link IJsonObject}.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class JsonObject implements IJsonObject
{
  private Map <String, IJson> m_aValues;

  public JsonObject ()
  {
    this (16);
  }

  public JsonObject (@Nonnegative final int nInitialCapacity)
  {
    m_aValues = new LinkedHashMap <String, IJson> (nInitialCapacity);
  }

  private void writeObject (@Nonnull final ObjectOutputStream aOOS) throws IOException
  {
    aOOS.writeInt (m_aValues.size ());
    final String sJson = JsonWriter.getAsString (this);
    aOOS.writeUTF (sJson);
  }

  private void readObject (@Nonnull final ObjectInputStream aOIS) throws IOException
  {
    final int nInitialSize = aOIS.readInt ();
    m_aValues = new LinkedHashMap <String, IJson> (nInitialSize);
    final String sJson = aOIS.readUTF ();
    try
    {
      JsonReader.parseAsObject (sJson, this);
    }
    catch (final JSONParsingException ex)
    {
      throw new IOException ("Failed to deserialize Json string '" + sJson + "'", ex);
    }
  }

  public boolean isArray ()
  {
    return false;
  }

  public boolean isObject ()
  {
    return true;
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
  public Iterator <Entry <String, IJson>> iterator ()
  {
    return m_aValues.entrySet ().iterator ();
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, @Nonnull final IJson aValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      throw new NullPointerException ("value");

    m_aValues.put (sName, aValue);
    return this;
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, @Nullable final Object aValue)
  {
    final IJson aJson = JsonConverter.convertToJson (aValue);
    return add (sName, aJson);
  }

  @Nonnull
  public JsonObject add (@Nonnull final Map.Entry <String, ?> aEntry)
  {
    return add (aEntry.getKey (), aEntry.getValue ());
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final boolean bValue)
  {
    return add (sName, JsonValue.create (bValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final byte nValue)
  {
    return add (sName, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final char cValue)
  {
    return add (sName, JsonValue.create (cValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final double dValue)
  {
    return add (sName, JsonValue.create (dValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final float fValue)
  {
    return add (sName, JsonValue.create (fValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final int nValue)
  {
    return add (sName, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final long nValue)
  {
    return add (sName, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonObject add (@Nonnull @Nonempty final String sName, final short nValue)
  {
    return add (sName, JsonValue.create (nValue));
  }

  @Nonnull
  public JsonObject addAll (@Nonnull final Map <String, ?> aMap)
  {
    if (aMap == null)
      throw new NullPointerException ("map");

    for (final Map.Entry <String, ?> aEntry : aMap.entrySet ())
      add (aEntry.getKey (), aEntry.getValue ());
    return this;
  }

  @Nonnull
  public JsonObject addAll (@Nonnull final IJsonObject aObject)
  {
    if (aObject == null)
      throw new NullPointerException ("object");

    for (final Map.Entry <String, IJson> aEntry : aObject)
      add (aEntry.getKey (), aEntry.getValue ());
    return this;
  }

  @Nullable
  public IJson removeKeyAndReturnValue (@Nullable final String sName)
  {
    return m_aValues.remove (sName);
  }

  @Nonnull
  public EChange removeKey (@Nullable final String sName)
  {
    return EChange.valueOf (m_aValues.remove (sName) != null);
  }

  public boolean containsKey (@Nullable final String sName)
  {
    return m_aValues.containsKey (sName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> keySet ()
  {
    return ContainerHelper.newSet (m_aValues.keySet ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <IJson> values ()
  {
    return ContainerHelper.newList (m_aValues.values ());
  }

  @Nullable
  public IJson get (@Nullable final String sName)
  {
    return m_aValues.get (sName);
  }

  @Nullable
  public IJsonValue getValue (@Nullable final String sName)
  {
    final IJson aJson = get (sName);
    return aJson != null && aJson.isValue () ? (IJsonValue) aJson : null;
  }

  @Nullable
  public IJsonArray getArray (@Nullable final String sName)
  {
    final IJson aJson = get (sName);
    return aJson != null && aJson.isArray () ? (IJsonArray) aJson : null;
  }

  @Nullable
  public IJsonObject getObject (@Nullable final String sName)
  {
    final IJson aJson = get (sName);
    return aJson != null && aJson.isObject () ? (IJsonObject) aJson : null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IJson> getAll ()
  {
    return ContainerHelper.newMap (m_aValues);
  }

  @Nonnull
  public JsonObject getClone ()
  {
    final JsonObject ret = new JsonObject (m_aValues.size ());
    for (final Map.Entry <String, IJson> aEntry : m_aValues.entrySet ())
      ret.add (aEntry.getKey (), aEntry.getValue ().getClone ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final JsonObject rhs = (JsonObject) o;
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
