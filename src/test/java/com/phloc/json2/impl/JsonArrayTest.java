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
package com.phloc.json2.impl;

import java.math.BigDecimal;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link JsonArray}.
 * 
 * @author Philip Helger
 */
public final class JsonArrayTest
{
  @Test
  public void testSerialize ()
  {
    final JsonArray aArray = new JsonArray ();
    PhlocTestUtils.testDefaultSerialization (aArray);
    aArray.add (5);
    PhlocTestUtils.testDefaultSerialization (aArray);
    aArray.add (3.141592653);
    PhlocTestUtils.testDefaultSerialization (aArray);
    aArray.add ("This is a string");
    PhlocTestUtils.testDefaultSerialization (aArray);
    aArray.add (new JsonArray ().add ("nested").add (0).add (BigDecimal.valueOf (12.34)));
    PhlocTestUtils.testDefaultSerialization (aArray);
    aArray.add (new JsonObject ().add ("n1", "nested").add ("n2", 0).add ("n3", BigDecimal.valueOf (12.34)));
    PhlocTestUtils.testDefaultSerialization (aArray);
  }
}
