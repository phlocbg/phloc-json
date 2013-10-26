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
package com.phloc.json2.serialize;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.json2.IJsonValueSerializer;

/**
 * Registry that determines the {@link IJsonValueSerializer} object to be used
 * for certain classes.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class JsonValueSerializerRegistry
{
  private static final JsonValueSerializerRegistry s_aInstance = new JsonValueSerializerRegistry ();
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // WeakHashMap because key is a class
  private static final Map <Class <?>, IJsonValueSerializer> s_aMap = new WeakHashMap <Class <?>, IJsonValueSerializer> ();

  static
  {
    getInstance ().registerJsonValueSerializer (CharSequence.class, JsonValueSerializerEscaped.getInstance ());
    getInstance ().registerJsonValueSerializer (String.class, JsonValueSerializerEscaped.getInstance ());
    getInstance ().registerJsonValueSerializer (StringBuilder.class, JsonValueSerializerEscaped.getInstance ());
    getInstance ().registerJsonValueSerializer (StringBuffer.class, JsonValueSerializerEscaped.getInstance ());
  }

  private JsonValueSerializerRegistry ()
  {}

  /**
   * @return The singleton instance of this class. Never <code>null</code>.
   */
  @Nonnull
  public static JsonValueSerializerRegistry getInstance ()
  {
    return s_aInstance;
  }

  /**
   * Register the value serializer for a certain class.
   * 
   * @param aClass
   *        The class to be registered. May not be <code>null</code>.
   * @param aValueSerializer
   *        The value serializer. May not be <code>null</code>.
   */
  public void registerJsonValueSerializer (@Nonnull final Class <?> aClass,
                                           @Nonnull final IJsonValueSerializer aValueSerializer)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    if (aValueSerializer == null)
      throw new NullPointerException ("valueSerializer");

    s_aRWLock.writeLock ().lock ();
    try
    {
      // The class should not already be registered
      if (s_aMap.containsKey (aClass))
        throw new IllegalArgumentException ("An IJsonValueSerializer for class " + aClass + " is already registered!");

      // register the class
      s_aMap.put (aClass, aValueSerializer);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static IJsonValueSerializer getJsonValueSerializer (@Nullable final Class <?> aSrcClass)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aMap.get (aSrcClass);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnegative
  public static int getRegisteredJsonValueSerializerCount ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aMap.size ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
