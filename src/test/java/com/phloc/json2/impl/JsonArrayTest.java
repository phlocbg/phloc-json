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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.json2.IJsonArray;
import com.phloc.json2.serialize.JsonWriter;

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

  @Test
  public void testGetSubArray ()
  {
    final JsonArray aArray = new JsonArray ();
    for (int i = 1; i <= 10; ++i)
      aArray.add (i);
    assertEquals ("[1,2,3,4,5,6,7,8,9,10]", JsonWriter.getAsString (aArray));

    final IJsonArray aSubArray = aArray.getSubArray (1, 5);
    assertEquals ("[2,3,4,5]", JsonWriter.getAsString (aSubArray));

    assertEquals ("[10]", JsonWriter.getAsString (aArray.getSubArray (9, 10)));
    assertEquals ("[]", JsonWriter.getAsString (aArray.getSubArray (0, 0)));

    aArray.removeAtIndex (1);
    assertEquals ("[1,3,4,5,6,7,8,9,10]", JsonWriter.getAsString (aArray));
    assertEquals ("[2,3,4,5]", JsonWriter.getAsString (aSubArray));
  }
}
