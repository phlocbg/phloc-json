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
package com.phloc.json2.convert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.json2.IJson;
import com.phloc.json2.JsonValue;
import com.phloc.json2.convert.IJsonConverter;
import com.phloc.json2.convert.IJsonConverterRegistrarSPI;
import com.phloc.json2.convert.IJsonConverterRegistry;

/**
 * Registrar for the default JSON type converters
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public class DefaultJsonConverterRegistrarSPI implements IJsonConverterRegistrarSPI
{
  private static class JsonValueConverter <DATATYPE> implements IJsonConverter <DATATYPE>
  {
    public IJson convertToJson (final DATATYPE aValue)
    {
      return JsonValue.create (aValue);
    }
  }

  public void registerJsonConverter (@Nonnull final IJsonConverterRegistry aRegistry)
  {
    aRegistry.registerJsonTypeConverter (Boolean.class, new JsonValueConverter <Boolean> ());
    aRegistry.registerJsonTypeConverter (Byte.class, new JsonValueConverter <Byte> ());
    aRegistry.registerJsonTypeConverter (Character.class, new JsonValueConverter <Character> ());
    aRegistry.registerJsonTypeConverter (Double.class, new JsonValueConverter <Double> ());
    aRegistry.registerJsonTypeConverter (Float.class, new JsonValueConverter <Float> ());
    aRegistry.registerJsonTypeConverter (Integer.class, new JsonValueConverter <Integer> ());
    aRegistry.registerJsonTypeConverter (Long.class, new JsonValueConverter <Long> ());
    aRegistry.registerJsonTypeConverter (Short.class, new JsonValueConverter <Short> ());

    aRegistry.registerJsonTypeConverter (String.class, new JsonValueConverter <String> ());
    aRegistry.registerJsonTypeConverter (StringBuilder.class, new JsonValueConverter <StringBuilder> ());
    aRegistry.registerJsonTypeConverter (StringBuffer.class, new JsonValueConverter <StringBuffer> ());

    aRegistry.registerJsonTypeConverter (BigDecimal.class, new JsonValueConverter <BigDecimal> ());
    aRegistry.registerJsonTypeConverter (BigInteger.class, new JsonValueConverter <BigInteger> ());
    aRegistry.registerJsonTypeConverter (AtomicBoolean.class, new JsonValueConverter <AtomicBoolean> ());
    aRegistry.registerJsonTypeConverter (AtomicInteger.class, new JsonValueConverter <AtomicInteger> ());
    aRegistry.registerJsonTypeConverter (AtomicLong.class, new JsonValueConverter <AtomicLong> ());
  }
}
