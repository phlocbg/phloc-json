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
package com.phloc.json2.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nullable;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.json2.IJson;
import com.phloc.json2.convert.JsonConverter;

/**
 * Test class for class {@link JsonWriter}.
 * 
 * @author Philip Helger
 */
public final class JsonReaderTest
{
  private void _testWriteAndRead (@Nullable final Object aValue)
  {
    final IJson aJson = JsonConverter.convertToJson (aValue);
    assertNotNull ("Failed: " + aValue, aJson);
    final String sJson = JsonWriter.getAsString (aJson);
    assertNotNull (sJson);
    final IJson aJsonRead = com.phloc.json2.parser.JsonReader.readFromString (sJson);
    assertNotNull (aJsonRead);
    final String sJsonRead = JsonWriter.getAsString (aJsonRead);
    assertNotNull (sJsonRead);
    assertEquals (sJson, sJsonRead);
  }

  @Test
  public void testSimpleValues ()
  {
    _testWriteAndRead (null);
    _testWriteAndRead (Boolean.TRUE);
    _testWriteAndRead (Byte.valueOf ((byte) 0));
    _testWriteAndRead (Byte.valueOf (Byte.MIN_VALUE));
    _testWriteAndRead (Byte.valueOf (Byte.MAX_VALUE));
    _testWriteAndRead (Character.toString ('6'));
    _testWriteAndRead (Character.toString (Character.MIN_VALUE));
    _testWriteAndRead (Character.toString (Character.MAX_VALUE));
    _testWriteAndRead (BigDecimal.valueOf (3.1415D));
    _testWriteAndRead (BigDecimal.valueOf (Double.MIN_VALUE));
    _testWriteAndRead (BigDecimal.valueOf (Double.MAX_VALUE));
    _testWriteAndRead (BigDecimal.valueOf (3.1415F));
    _testWriteAndRead (BigDecimal.valueOf (Float.MIN_VALUE));
    _testWriteAndRead (BigDecimal.valueOf (Float.MAX_VALUE));
    _testWriteAndRead (Integer.valueOf (15));
    _testWriteAndRead (Integer.valueOf (Integer.MIN_VALUE));
    _testWriteAndRead (Integer.valueOf (Integer.MAX_VALUE));
    _testWriteAndRead (Long.valueOf (15L));
    _testWriteAndRead (Long.valueOf (Long.MIN_VALUE));
    _testWriteAndRead (Long.valueOf (Long.MAX_VALUE));
    _testWriteAndRead (Short.valueOf ((short) 15));
    _testWriteAndRead (Short.valueOf (Short.MIN_VALUE));
    _testWriteAndRead (Short.valueOf (Short.MAX_VALUE));
    _testWriteAndRead ("a string");
    _testWriteAndRead (new StringBuilder ().append ("string builder"));
    _testWriteAndRead (new StringBuffer ().append ("string buffer"));
    _testWriteAndRead (BigDecimal.ONE);
    _testWriteAndRead (BigInteger.ONE);
    _testWriteAndRead (new AtomicBoolean (true));
    _testWriteAndRead (new AtomicInteger (15));
    _testWriteAndRead (new AtomicLong (15L));
  }

  @Test
  public void testArray ()
  {
    _testWriteAndRead (new boolean [] { true, false, true });
    _testWriteAndRead (new byte [] { 0, 1, 2 });
    _testWriteAndRead (new char [] { 'a', 'b', 'c' });
    _testWriteAndRead (new double [] { 1, 2, 3 });
    _testWriteAndRead (new float [] { 1, 2, 3 });
    _testWriteAndRead (new int [] { 1, 2, 3 });
    _testWriteAndRead (new long [] { 1, 2, 3 });
    _testWriteAndRead (new short [] { 1, 2, 3 });
    _testWriteAndRead (new String [] { "foo", "bar", "bla" });
    _testWriteAndRead (ContainerHelper.<Object> newList (BigDecimal.ONE, BigInteger.ZERO, BigDecimal.TEN));
  }

  @Test
  public void testMap ()
  {
    final Map <String, Object> aMap = new HashMap <String, Object> ();
    aMap.put ("foo", "bar");
    _testWriteAndRead (aMap);

    final Map <String, Object> aTreeMap = new TreeMap <String, Object> ();
    aTreeMap.put ("foo", "bar");
    aTreeMap.put ("foo2", Integer.valueOf (5));
    _testWriteAndRead (aTreeMap);

    final Map <String, Object> aLinkedMap = new LinkedHashMap <String, Object> ();
    aLinkedMap.put ("foo", "bar");
    aLinkedMap.put ("foo2", Integer.valueOf (5));
    _testWriteAndRead (aLinkedMap);
  }
}
