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

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.json2.serialize.JsonValueSerializerEscaped;

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
  public void testSerialize ()
  {
    PhlocTestUtils.testDefaultSerialization (JsonValue.create (true));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create (false));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create ((byte) 5));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create ('x'));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create (3.14d));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create (3.14f));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create (47));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create (Long.MAX_VALUE));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create ((short) 815));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create ("Test", JsonValueSerializerEscaped.getInstance ()));
    PhlocTestUtils.testDefaultSerialization (JsonValue.create ("bla foo fasel",
                                                               JsonValueSerializerEscaped.getInstance ()));
  }
}
