/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.json.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Test class for class {@link JSONObject}
 * 
 * @author philip
 */
public final class JSONObjectTest
{
  @Test
  public void testSimple () throws JSONParsingException
  {
    JSONObject aObj = new JSONObject ();
    aObj.setStringProperty ("a", "b");
    assertEquals ("{\"a\":\"b\"}", aObj.getJSONString ());
    assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setBigDecimalProperty ("x", new BigDecimal ("1514131211.0987654321"));
    assertEquals ("{\"x\":1514131211.0987654321}", aObj.getJSONString ());
    assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setBigIntegerProperty ("a", new BigInteger ("1514131288877788877711"));
    assertEquals ("{\"a\":1514131288877788877711}", aObj.getJSONString ());
    assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setBooleanProperty ("a", false);
    assertEquals ("{\"a\":false}", aObj.getJSONString ());
    assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setDoubleProperty ("a", 123.45);
    assertEquals ("{\"a\":123.45}", aObj.getJSONString ());
    // Parsed object will be BigDecimal!
    if (false)
      assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setFunctionProperty ("a", "alert;", "foo", "bar");
    assertEquals ("{\"a\":function(foo,bar){alert;}}", aObj.getJSONString ());
    if (false)
      assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setFunctionPrebuildProperty ("a", " function (page) { alert (page + \" of 17\"); } ");
    assertEquals ("{\"a\": function (page) { alert (page + \" of 17\"); } }", aObj.getJSONString ());
    if (false)
      assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setIntegerProperty ("a", 15);
    assertEquals ("{\"a\":15}", aObj.getJSONString ());
    assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setKeywordProperty ("a", "null");
    assertEquals ("{\"a\":null}", aObj.getJSONString ());
    // Special handling for null and keyword
    if (false)
      assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setLongProperty ("a", 12345678901L);
    assertEquals ("{\"a\":12345678901}", aObj.getJSONString ());
    assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setDoubleProperty ("abc", 15.34);
    assertEquals ("{\"abc\":15.34}", aObj.getJSONString ());
    assertEquals (Double.valueOf (15.34), aObj.getDoubleProperty ("abc"));
    aObj.setDoubleProperty ("def", Double.valueOf (16.79));
    assertEquals ("{\"abc\":15.34,\"def\":16.79}", aObj.getJSONString ());
    assertEquals (Double.valueOf (15.34), aObj.getDoubleProperty ("abc"));
    assertEquals (Double.valueOf (16.79), aObj.getDoubleProperty ("def"));
    // Will be parsed as BigDecimal
    if (false)
      assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));
  }
}
