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

import java.math.BigDecimal;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link JsonObject}.
 * 
 * @author Philip Helger
 */
public final class JsonObjectTest
{
  @Test
  public void testSerialize ()
  {
    final JsonObject aObject = new JsonObject ();
    PhlocTestUtils.testDefaultSerialization (aObject);
    aObject.add ("key1", 5);
    PhlocTestUtils.testDefaultSerialization (aObject);
    aObject.add ("key2", 3.141592653);
    PhlocTestUtils.testDefaultSerialization (aObject);
    aObject.add ("key3", "This is a string");
    PhlocTestUtils.testDefaultSerialization (aObject);
    aObject.add ("key4", new JsonArray ().add ("nested").add (0).add (BigDecimal.valueOf (12.34)));
    PhlocTestUtils.testDefaultSerialization (aObject);
    aObject.add ("key5", new JsonObject ().add ("n1", "nested").add ("n2", 0).add ("n3", BigDecimal.valueOf (12.34)));
    PhlocTestUtils.testDefaultSerialization (aObject);
  }
}
