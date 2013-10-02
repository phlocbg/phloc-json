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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.phloc.commons.typeconvert.TypeConverterException;
import com.phloc.json2.convert.JsonConverter;

/**
 * Test class for class {@link JsonValue}.
 * 
 * @author Philip Helger
 */
public final class JsonValueTest
{
  @Test
  public void testCache ()
  {
    for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; ++i)
      assertNotNull (JsonValue.create ((byte) i));
    for (int i = -200; i < 200; ++i)
      assertNotNull (JsonValue.create (i));
    for (long i = -200; i < 200; ++i)
      assertNotNull (JsonValue.create (i));
  }

  @Test
  public void testDefaultSPI ()
  {
    assertNotNull (JsonConverter.convertToJson (Boolean.TRUE));
    assertNotNull (JsonConverter.convertToJson (Byte.valueOf ((byte) 0)));
    assertNotNull (JsonConverter.convertToJson (Character.valueOf ('\0')));
    assertNotNull (JsonConverter.convertToJson (Double.valueOf (3.1415D)));
    assertNotNull (JsonConverter.convertToJson (Float.valueOf (3.1415F)));
    assertNotNull (JsonConverter.convertToJson (Integer.valueOf (15)));
    assertNotNull (JsonConverter.convertToJson (Long.valueOf (15L)));
    assertNotNull (JsonConverter.convertToJson (Short.valueOf ((short) 15)));
    assertNotNull (JsonConverter.convertToJson ("a string"));
    assertNotNull (JsonConverter.convertToJson (new StringBuilder ().append ("sb")));
    assertNotNull (JsonConverter.convertToJson (new StringBuffer ().append ("sb")));
    assertNotNull (JsonConverter.convertToJson (BigDecimal.ONE));
    assertNotNull (JsonConverter.convertToJson (BigInteger.ONE));
    assertNotNull (JsonConverter.convertToJson (new AtomicBoolean (true)));
    assertNotNull (JsonConverter.convertToJson (new AtomicInteger (15)));
    assertNotNull (JsonConverter.convertToJson (new AtomicLong (15L)));

    try
    {
      // No converter registered!
      JsonConverter.convertToJson (new JsonValueTest ());
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
  }
}
