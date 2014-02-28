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
package com.phloc.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.phloc.json.impl.CJSONConstants;
import com.phloc.json.impl.value.JSONPropertyValueKeyword;

public final class JSONComplexUtilsTest
{
  private static final String PROP1 = "Property1";
  private static final String PROP2 = "Property2";
  private static final String PROP3 = "Property3";
  private static final String PROP4 = "Property4";
  private static final String PROP5 = "Property5";

  private static final String VALUE_STRING = "VALUE";
  private static final Integer VALUE_INTEGER = Integer.valueOf (42);
  private static final Double VALUE_DOUBLE = Double.valueOf (3.2);
  private static final Boolean VALUE_BOOLEAN = Boolean.FALSE;

  @Test
  public void testConvertToJSON ()
  {
    // test empty (null) property map
    {
      final IJSONObject aObj = JSONComplexUtils.convertToJSON (null);
      assertNotNull (aObj);
      assertEquals (aObj.getAllPropertyNames ().size (), 0);
    }

    // test mixed types
    {
      final Map <String, Object> aProperties = new HashMap <String, Object> ();
      aProperties.put (PROP1, VALUE_STRING);
      aProperties.put (PROP2, VALUE_INTEGER);
      aProperties.put (PROP3, VALUE_DOUBLE);
      aProperties.put (PROP4, VALUE_BOOLEAN);
      aProperties.put (PROP5, null);
      final IJSONObject aObj = JSONComplexUtils.convertToJSON (aProperties);
      assertNotNull (aObj);
      assertEquals (aObj.getAllPropertyNames ().size (), aProperties.size ());
      assertEquals (aObj.getProperty (PROP1).getValue ().getData (), VALUE_STRING);
      assertEquals (aObj.getProperty (PROP2).getValue ().getData (), VALUE_INTEGER);
      assertEquals (aObj.getProperty (PROP3).getValue ().getData (), VALUE_DOUBLE);
      assertEquals (aObj.getProperty (PROP4).getValue ().getData (), VALUE_BOOLEAN);
      assertTrue (aObj.getProperty (PROP5).getValue () instanceof JSONPropertyValueKeyword);
      assertEquals (aObj.getProperty (PROP5).getValue ().getData (), CJSONConstants.KEYWORD_NULL);
    }
  }
}
