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
package com.phloc.json2.impl;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nullable;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.json2.IJson;
import com.phloc.json2.convert.JsonConverter;
import com.phloc.json2.parser.JsonReader;
import com.phloc.json2.serialize.JsonWriter;

/**
 * Test class for equals and hashCode.
 * 
 * @author Philip Helger
 */
public final class EqualsHashcodeTest
{
  private void _testEqualsHashcode (@Nullable final Object aValue)
  {
    final IJson aJson = JsonConverter.convertToJson (aValue);
    assertNotNull ("Failed: " + aValue, aJson);
    final String sJson = JsonWriter.getAsString (aJson);
    assertNotNull (sJson);
    final IJson aJsonRead = JsonReader.readFromString (sJson);
    assertNotNull (aJsonRead);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aJson, aJsonRead);
  }

  @Test
  public void testSimpleValues ()
  {
    _testEqualsHashcode (BigInteger.valueOf (15));
    _testEqualsHashcode (BigInteger.valueOf (Long.MAX_VALUE));
    _testEqualsHashcode ("a string");
    _testEqualsHashcode (BigDecimal.valueOf (3.1415));
  }

  @Test
  public void testArray ()
  {
    _testEqualsHashcode (new boolean [] { true, false, true });
    _testEqualsHashcode (new byte [] { 0, 1, 2 });
    _testEqualsHashcode (new char [] { 'a', 'b', 'c' });
    _testEqualsHashcode (new int [] { 1, 2, 3 });
    _testEqualsHashcode (new long [] { 1, 2, 3 });
    _testEqualsHashcode (new short [] { 1, 2, 3 });
    _testEqualsHashcode (new String [] { "foo", "bar", "bla" });
    _testEqualsHashcode (ContainerHelper.<Object> newList (BigDecimal.valueOf (3.1415),
                                                           BigDecimal.valueOf (4.1415),
                                                           BigDecimal.valueOf (5.1415)));
  }

  @Test
  public void testMap ()
  {
    final Map <String, Object> aMap = new HashMap <String, Object> ();
    aMap.put ("foo", "bar");
    _testEqualsHashcode (aMap);

    final Map <String, Object> aTreeMap = new TreeMap <String, Object> ();
    aTreeMap.put ("foo", "bar");
    aTreeMap.put ("foo2", BigInteger.valueOf (5));
    _testEqualsHashcode (aTreeMap);

    final Map <String, Object> aLinkedMap = new LinkedHashMap <String, Object> ();
    aLinkedMap.put ("foo", "bar");
    aLinkedMap.put ("foo2", BigInteger.valueOf (5));
    _testEqualsHashcode (aLinkedMap);
  }
}
