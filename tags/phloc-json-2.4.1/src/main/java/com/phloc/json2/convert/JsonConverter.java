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

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.json2.IJson;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;
import com.phloc.json2.impl.JsonValue;

/**
 * A utility class for converting objects from and to {@link IJson}.
 * 
 * @author Philip Helger
 */
@Immutable
public final class JsonConverter
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final JsonConverter s_aInstance = new JsonConverter ();

  private JsonConverter ()
  {}

  @Nonnull
  public static IJson convertToJson (@Nullable final Object aObject)
  {
    if (aObject == null)
      return JsonValue.NULL;

    if (aObject instanceof IJson)
      return (IJson) aObject;

    if (ArrayHelper.isArray (aObject))
    {
      if (aObject instanceof boolean [])
      {
        final boolean [] aArray = (boolean []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final boolean bValue : aArray)
          aJsonArray.add (bValue);
        return aJsonArray;
      }
      if (aObject instanceof byte [])
      {
        final byte [] aArray = (byte []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final byte nValue : aArray)
          aJsonArray.add (nValue);
        return aJsonArray;
      }
      if (aObject instanceof char [])
      {
        final char [] aArray = (char []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final char cValue : aArray)
          aJsonArray.add (cValue);
        return aJsonArray;
      }
      if (aObject instanceof double [])
      {
        final double [] aArray = (double []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final double dValue : aArray)
          aJsonArray.add (dValue);
        return aJsonArray;
      }
      if (aObject instanceof float [])
      {
        final float [] aArray = (float []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final float fValue : aArray)
          aJsonArray.add (fValue);
        return aJsonArray;
      }
      if (aObject instanceof int [])
      {
        final int [] aArray = (int []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final int nValue : aArray)
          aJsonArray.add (nValue);
        return aJsonArray;
      }
      if (aObject instanceof long [])
      {
        final long [] aArray = (long []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final long nValue : aArray)
          aJsonArray.add (nValue);
        return aJsonArray;
      }
      if (aObject instanceof short [])
      {
        final short [] aArray = (short []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final short nValue : aArray)
          aJsonArray.add (nValue);
        return aJsonArray;
      }
      if (aObject instanceof Object [])
      {
        final Object [] aArray = (Object []) aObject;
        final JsonArray aJsonArray = new JsonArray (aArray.length);
        for (final Object aValue : aArray)
        {
          // Recursive conversion
          aJsonArray.add (convertToJson (aValue));
        }
        return aJsonArray;
      }
      throw new IllegalStateException ("Expected an array but got none. Object=" + aObject);
    }

    if (aObject instanceof Collection <?>)
    {
      final Collection <?> aCollection = (Collection <?>) aObject;
      final JsonArray aJsonArray = new JsonArray (aCollection.size ());
      for (final Object aValue : aCollection)
      {
        // Recursive conversion
        aJsonArray.add (convertToJson (aValue));
      }
      return aJsonArray;
    }

    if (aObject instanceof Map <?, ?>)
    {
      final Map <?, ?> aMap = (Map <?, ?>) aObject;
      final JsonObject aJsonObject = new JsonObject (aMap.size ());
      for (final Map.Entry <?, ?> aEntry : aMap.entrySet ())
      {
        final String sKey = TypeConverter.convertIfNecessary (aEntry.getKey (), String.class);
        // Recursive conversion
        final IJson aValue = convertToJson (aEntry.getValue ());
        aJsonObject.add (sKey, aValue);
      }
      return aJsonObject;
    }

    // If no converter was found, assume it is a JsonValue
    return JsonValue.create (aObject);
  }
}
