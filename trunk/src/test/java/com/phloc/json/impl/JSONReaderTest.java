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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocAssert;
import com.phloc.json.AbstractJSONTestCase;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.impl.JSONReader;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueString;

/**
 * This is a test for basic JSON object unmarshalling and for testing the
 * {@link JSONReader}.
 * 
 * @author Boris Gregorcic
 */
public final class JSONReaderTest extends AbstractJSONTestCase
{
  /**
   * Tests parsing a simple JSON object using the {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseObjectSimple () throws Exception
  {
    assertEquals (m_aSimpleObject, JSONReader.parseObject (m_aSimpleObject.getJSONString ()));
  }

  /**
   * Tests parsing a complex JSON object including a nested object using the
   * {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseObjectComplex () throws Exception
  {
    assertEquals (JSONReader.parseObject (m_aComplexObject.getJSONString ()), m_aComplexObject);
  }

  /**
   * Tests parsing a JSON string array into a property value list using the
   * {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseArrayText () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[\"" + STR_VALUE1 + "\", \"" + STR_VALUE2 + "\", \"" + STR_VALUE3 + "\"]"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 3);
    assertEquals (aJSONList.getValues ().get (0).getData (), STR_VALUE1);
    assertEquals (aJSONList.getValues ().get (1).getData (), STR_VALUE2);
    assertEquals (aJSONList.getValues ().get (2).getData (), STR_VALUE3);
  }

  /**
   * Tests parsing a JSON boolean array into a property value list using the
   * {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseArrayBoolean () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[" + Boolean.TRUE + ", " + Boolean.FALSE + ", " + Boolean.TRUE + "]"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 3);
    assertEquals (aJSONList.getValues ().get (0).getData (), Boolean.TRUE);
    assertEquals (aJSONList.getValues ().get (1).getData (), Boolean.FALSE);
    assertEquals (aJSONList.getValues ().get (2).getData (), Boolean.TRUE);
  }

  /**
   * Tests parsing a JSON integer array into a property value list using the
   * {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseArrayInteger () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[" + VALUE_INT1 + ", " + VALUE_INT2 + ", " + VALUE_INT3 + "]"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 3);
    assertEquals (aJSONList.getValues ().get (0).getData (), Integer.valueOf (VALUE_INT1));
    assertEquals (aJSONList.getValues ().get (1).getData (), Integer.valueOf (VALUE_INT2));
    assertEquals (aJSONList.getValues ().get (2).getData (), Integer.valueOf (VALUE_INT3));
  }

  /**
   * Tests parsing an empty JSON array into a property value list using the
   * {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseArrayEmpty () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[]");
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 0);
  }

  /**
   * Tests parsing an empty JSON array into a property value list using the
   * {@link JSONReader}
   * 
   * @throws Exception
   */
  @Test
  public void testParseArrayEmptyString () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[\"\"]");
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 1);
  }

  /**
   * Tests parsing a JSON string value using the automated type detection
   * 
   * @throws Exception
   */
  @Test
  public void testAutoParseString () throws Exception
  {
    final IJSON aJSON = JSONReader.parse ("\"" + VALUE_ONE + "\""); //$NON-NLS-2$ 
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof JSONPropertyValueString);
    assertEquals (((JSONPropertyValueString) aJSON).getData (), VALUE_ONE);
  }

  /**
   * Tests parsing a JSON integer value using the automated type detection
   * 
   * @throws Exception
   */
  @Test
  public void testAutoParseInteger () throws Exception
  {
    final IJSON aJSON = JSONReader.parse (String.valueOf (VALUE_INT1));
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof JSONPropertyValueInteger);
    assertNotNull (((JSONPropertyValueInteger) aJSON).getData ());
    assertEquals (((JSONPropertyValueInteger) aJSON).getData ().intValue (), VALUE_INT1);
  }

  /**
   * Tests parsing a JSON boolean value using the automated type detection
   * 
   * @throws Exception
   */
  @Test
  public void testAutoParseBoolean () throws Exception
  {
    final IJSON aJSON = JSONReader.parse (String.valueOf (VALUE_BOOL));
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof JSONPropertyValueBoolean);
    assertNotNull (((JSONPropertyValueBoolean) aJSON).getData ());
    assertTrue (((JSONPropertyValueBoolean) aJSON).getData ().booleanValue () == VALUE_BOOL);
  }

  /**
   * Tests parsing a JSON double value using the automated type detection
   * 
   * @throws Exception
   */
  @Test
  public void testAutoParseDouble () throws Exception
  {
    final IJSON aJSON = JSONReader.parse (String.valueOf (VALUE_DOUBLE));
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof JSONPropertyValueBigDecimal);
    assertNotNull (((JSONPropertyValueBigDecimal) aJSON).getData ());
    PhlocAssert.assertEquals (((JSONPropertyValueBigDecimal) aJSON).getData ().doubleValue (),
                              Double.valueOf (VALUE_DOUBLE));
  }

  /**
   * Tests parsing a JSON array value using the automated type detection
   * 
   * @throws Exception
   */
  @Test
  public void testAutoParseArray () throws Exception
  {
    final IJSON aJSON = JSONReader.parse ("[" + VALUE_INT1 + ", " + VALUE_INT2 + ", " + VALUE_INT3 + "]"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof IJSONPropertyValueList <?>);
    final IJSONPropertyValueList <?> aJSONList = (IJSONPropertyValueList <?>) aJSON;
    assertEquals (aJSONList.getValues ().size (), 3);
    assertEquals (aJSONList.getValues ().get (0).getData (), Integer.valueOf (VALUE_INT1));
    assertEquals (aJSONList.getValues ().get (1).getData (), Integer.valueOf (VALUE_INT2));
    assertEquals (aJSONList.getValues ().get (2).getData (), Integer.valueOf (VALUE_INT3));
  }

  /**
   * Tests parsing a JSON object value using the automated type detection
   * 
   * @throws Exception
   */
  @Test
  public void testAutoParseObject () throws Exception
  {
    final IJSON aJSON = JSONReader.parse (m_aComplexObject.getJSONString ());
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof IJSONObject);
    assertEquals (aJSON, m_aComplexObject);
  }
}
