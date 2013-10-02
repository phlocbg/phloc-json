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
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.typeconvert.TypeConverterException;
import com.phloc.commons.typeconvert.TypeConverterException.EReason;
import com.phloc.json2.IJson;
import com.phloc.json2.JsonValue;

/**
 * A utility class for converting objects from and to {@link IJson}.<br>
 * All converters are registered in the {@link JsonConverterRegistry}.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class JsonConverter
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final JsonConverter s_aInstance = new JsonConverter ();

  private JsonConverter ()
  {}

  @Nullable
  public static IJson convertToJson (@Nullable final Object aObject) throws TypeConverterException
  {
    if (aObject == null)
      return JsonValue.NULL;

    // Lookup converter
    final Class <?> aSrcClass = aObject.getClass ();
    final IJsonConverter <?> aConverter = JsonConverterRegistry.getConverterToJson (aSrcClass);
    if (aConverter == null)
      throw new TypeConverterException (aSrcClass, IJson.class, EReason.NO_CONVERTER_FOUND);

    // Perform conversion
    @SuppressWarnings ("unchecked")
    final IJson ret = ((IJsonConverter <Object>) aConverter).convertToJson (aObject);
    if (ret == null)
      throw new TypeConverterException (aSrcClass, IJson.class, EReason.CONVERSION_FAILED);
    return ret;
  }

  @Nullable
  public static <DSTTYPE> DSTTYPE convertToNative (@Nullable final IJson aElement,
                                                   @Nonnull final Class <DSTTYPE> aDstClass) throws TypeConverterException
  {
    if (aDstClass == null)
      throw new NullPointerException ("destinationClass");

    if (aElement == null)
      return null;

    // Lookup converter
    final IJsonConverter <?> aConverter = JsonConverterRegistry.getConverterToNative (aDstClass);
    if (aConverter == null)
      throw new TypeConverterException (IJson.class, aDstClass, EReason.NO_CONVERTER_FOUND);

    // Perform conversion
    final DSTTYPE ret = aDstClass.cast (aConverter.convertToNative (aElement));
    if (ret == null)
      throw new TypeConverterException (IJson.class, aDstClass, EReason.CONVERSION_FAILED);
    return ret;
  }
}
