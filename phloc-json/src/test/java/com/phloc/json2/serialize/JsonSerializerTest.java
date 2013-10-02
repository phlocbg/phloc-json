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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.json2.convert.JsonConverter;

/**
 * Test class for class {@link JsonSerializer}.
 * 
 * @author Philip Helger
 */
public final class JsonSerializerTest
{
  @Test
  public void testSimpleValues ()
  {
    assertEquals ("true", JsonSerializer.getAsString (JsonConverter.convertToJson (Boolean.TRUE)));
    assertEquals ("0", JsonSerializer.getAsString (JsonConverter.convertToJson (Byte.valueOf ((byte) 0))));
    assertEquals ("\0", JsonSerializer.getAsString (JsonConverter.convertToJson (Character.valueOf ('\0'))));
    assertEquals ("3.1415", JsonSerializer.getAsString (JsonConverter.convertToJson (Double.valueOf (3.1415D))));
    assertEquals ("3.1415", JsonSerializer.getAsString (JsonConverter.convertToJson (Float.valueOf (3.1415F))));
    assertEquals ("15", JsonSerializer.getAsString (JsonConverter.convertToJson (Integer.valueOf (15))));
    assertEquals ("15", JsonSerializer.getAsString (JsonConverter.convertToJson (Long.valueOf (15L))));
    assertEquals ("15", JsonSerializer.getAsString (JsonConverter.convertToJson (Short.valueOf ((short) 15))));
    assertEquals ("\"a string\"", JsonSerializer.getAsString (JsonConverter.convertToJson ("a string")));
    assertEquals ("\"sb\"",
                  JsonSerializer.getAsString (JsonConverter.convertToJson (new StringBuilder ().append ("sb"))));
    assertEquals ("\"sb\"",
                  JsonSerializer.getAsString (JsonConverter.convertToJson (new StringBuffer ().append ("sb"))));
    assertEquals ("1", JsonSerializer.getAsString (JsonConverter.convertToJson (BigDecimal.ONE)));
    assertEquals ("1", JsonSerializer.getAsString (JsonConverter.convertToJson (BigInteger.ONE)));
    assertEquals ("true", JsonSerializer.getAsString (JsonConverter.convertToJson (new AtomicBoolean (true))));
    assertEquals ("15", JsonSerializer.getAsString (JsonConverter.convertToJson (new AtomicInteger (15))));
    assertEquals ("15", JsonSerializer.getAsString (JsonConverter.convertToJson (new AtomicLong (15L))));
  }

  @Test
  public void testArray ()
  {
    assertEquals ("[true,false,true]",
                  JsonSerializer.getAsString (JsonConverter.convertToJson (new boolean [] { true, false, true })));
    assertEquals ("[0,1,2]", JsonSerializer.getAsString (JsonConverter.convertToJson (new byte [] { 0, 1, 2 })));
    assertEquals ("[\"a\",\"b\",\"c\"]",
                  JsonSerializer.getAsString (JsonConverter.convertToJson (new char [] { 'a', 'b', 'c' })));
    assertEquals ("[1.0,2.0,3.0]", JsonSerializer.getAsString (JsonConverter.convertToJson (new double [] { 1, 2, 3 })));
    assertEquals ("[1.0,2.0,3.0]", JsonSerializer.getAsString (JsonConverter.convertToJson (new float [] { 1, 2, 3 })));
    assertEquals ("[1,2,3]", JsonSerializer.getAsString (JsonConverter.convertToJson (new int [] { 1, 2, 3 })));
    assertEquals ("[1,2,3]", JsonSerializer.getAsString (JsonConverter.convertToJson (new long [] { 1, 2, 3 })));
    assertEquals ("[1,2,3]", JsonSerializer.getAsString (JsonConverter.convertToJson (new short [] { 1, 2, 3 })));
    assertEquals ("[\"foo\",\"bar\",\"bla\"]",
                  JsonSerializer.getAsString (JsonConverter.convertToJson (new String [] { "foo", "bar", "bla" })));
    assertEquals ("[1,0,10]",
                  JsonSerializer.getAsString (JsonConverter.convertToJson (ContainerHelper.<Object> newList (BigDecimal.ONE,
                                                                                                             BigInteger.ZERO,
                                                                                                             BigDecimal.TEN))));
  }

  @Test
  public void testMap ()
  {
    final Map <String, Object> aMap = new HashMap <String, Object> ();
    aMap.put ("foo", "bar");
    assertEquals ("{\"foo\":\"bar\"}", JsonSerializer.getAsString (JsonConverter.convertToJson (aMap)));

    final Map <String, Object> aTreeMap = new TreeMap <String, Object> ();
    aTreeMap.put ("foo", "bar");
    aTreeMap.put ("foo2", Integer.valueOf (5));
    assertEquals ("{\"foo\":\"bar\",\"foo2\":5}", JsonSerializer.getAsString (JsonConverter.convertToJson (aTreeMap)));

    final Map <String, Object> aLinkedMap = new LinkedHashMap <String, Object> ();
    aLinkedMap.put ("foo", "bar");
    aLinkedMap.put ("foo2", Integer.valueOf (5));
    assertEquals ("{\"foo\":\"bar\",\"foo2\":5}", JsonSerializer.getAsString (JsonConverter.convertToJson (aLinkedMap)));
  }
}
