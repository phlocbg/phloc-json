/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.PhlocAssert;
import com.phloc.commons.string.StringParser;
import com.phloc.json.AbstractJSONTestCase;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONProperty;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.JSONHelper;
import com.phloc.json.JSONHelperTest;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueDouble;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueKeyword;
import com.phloc.json.impl.value.JSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueString;

/**
 * This is a test for basic JSON object unmarshalling and for testing the
 * {@link JSONReader}.
 * 
 * @author Boris Gregorcic
 */
@SuppressWarnings ("static-method")
public final class JSONReaderTest extends AbstractJSONTestCase
{
  private static final Logger LOG = LoggerFactory.getLogger (JSONReaderTest.class);
  private static final String PROPERTY = "a"; //$NON-NLS-1$

  private static final String MOCK_JSON_LIB = "com/phloc/json/mock/mockLibrary.json"; //$NON-NLS-1$
  public static final String MOCK_JSON_LIB_FULL = "com/phloc/json/mock/mockLibraryFull.json"; //$NON-NLS-1$
  private static final String MOCK_JSON_LIB_MINI = "com/phloc/json/mock/mockLibraryMini.json"; //$NON-NLS-1$
  private static final String MOCK_JSON_MODEL = "com/phloc/json/mock/modelWithEndpoints.jsonill"; //$NON-NLS-1$

  /**
   * Tests parsing a simple JSON object using the {@link JSONReader}
   */
  @Test
  public void testParseObjectSimple () throws Exception
  {
    final IJSONObject aParsedObject = JSONReader.parseObject (this.m_aSimpleObject.getJSONString ());
    LOG.info (this.m_aSimpleObject.getJSONString ());
    LOG.info (aParsedObject.getJSONString ());
    if (aParsedObject.equals (this.m_aSimpleObject))
    {
      LOG.info ("glaich"); //$NON-NLS-1$
    }
    assertEquals (this.m_aSimpleObject, aParsedObject);
  }

  /**
   * Tests parsing a complex JSON object including a nested object using the
   * {@link JSONReader}
   */
  @Test
  public void testParseObjectComplex () throws Exception
  {
    final IJSONObject aParsedObject = JSONReader.parseObject (this.m_aComplexObject.getJSONString ());
    assertEquals (this.m_aComplexObject, aParsedObject);
  }

  @Test
  public void testInvalidJSONs ()
  {
    testForParseException (""); //$NON-NLS-1$
    testForParseException ("oida"); //$NON-NLS-1$
    testForParseException ("{"); //$NON-NLS-1$
    testForParseException ("{a:b,}"); //$NON-NLS-1$
    testForParseException ("{a:'b'}"); //$NON-NLS-1$
  }

  @Test
  public void testParseNull () throws JSONParsingException
  {
    final boolean bOldValue = JSONSettings.getInstance ().isParseNullValues ();
    try
    {
      JSONSettings.getInstance ().setParseNullValues (false);
      testForParseException ("null"); //$NON-NLS-1$
      JSONSettings.getInstance ().setParseNullValues (true);
      final IJSON aNull = JSONReader.parse ("null"); //$NON-NLS-1$
      Assert.assertTrue (aNull instanceof JSONPropertyValueKeyword);
      Assert.assertEquals (CJSONConstants.KEYWORD_NULL, ((JSONPropertyValueKeyword) aNull).getData ());
    }
    finally
    {
      JSONSettings.getInstance ().setParseNullValues (bOldValue);
    }
  }

  @Test
  public void testParseNullProperty () throws JSONParsingException
  {
    final boolean bOldValue = JSONSettings.getInstance ().isParseNullValues ();
    try
    {
      {
        JSONSettings.getInstance ().setParseNullValues (false);
        final IJSONObject aJSON = JSONReader.parseObject ("{a:\"a\", b:null}"); //$NON-NLS-1$
        final IJSON aNull = aJSON.getProperty ("b"); //$NON-NLS-1$
        Assert.assertNull (aNull);
      }
      {
        JSONSettings.getInstance ().setParseNullValues (true);
        final IJSONObject aJSON = JSONReader.parseObject ("{a:\"a\", b:null}"); //$NON-NLS-1$
        final IJSONProperty <?> aNull = aJSON.getProperty ("b"); //$NON-NLS-1$
        Assert.assertTrue (aNull.getValue () instanceof JSONPropertyValueKeyword);
        Assert.assertEquals (CJSONConstants.KEYWORD_NULL, ((JSONPropertyValueKeyword) aNull.getValue ()).getData ());
      }
    }
    finally
    {
      JSONSettings.getInstance ().setParseNullValues (bOldValue);
    }
  }

  @Test
  public void testParseNullListElem () throws JSONParsingException
  {
    final boolean bOldValue = JSONSettings.getInstance ().isParseNullValues ();
    try
    {
      {
        JSONSettings.getInstance ().setParseNullValues (false);
        final IJSONObject aJSON = JSONReader.parseObject ("{list:[\"a\", null]}"); //$NON-NLS-1$
        final IJSONProperty <?> aList = aJSON.getProperty ("list"); //$NON-NLS-1$
        Assert.assertTrue (aList.getValue () instanceof JSONPropertyValueList <?>);
        final List <?> aElements = ((JSONPropertyValueList <?>) aList.getValue ()).getData ();
        Assert.assertEquals (1, aElements.size ());
      }
      {
        JSONSettings.getInstance ().setParseNullValues (true);
        final IJSONObject aJSON = JSONReader.parseObject ("{list:[\"a\", null]}"); //$NON-NLS-1$
        final IJSONProperty <?> aList = aJSON.getProperty ("list"); //$NON-NLS-1$
        Assert.assertTrue (aList.getValue () instanceof JSONPropertyValueList <?>);
        {
          final List <?> aElements = ((JSONPropertyValueList <?>) aList.getValue ()).getData ();
          Assert.assertEquals (2, aElements.size ());
          Assert.assertTrue (aElements.get (1) instanceof JSONPropertyValueKeyword);
          Assert.assertEquals (CJSONConstants.KEYWORD_NULL, ((JSONPropertyValueKeyword) aElements.get (1)).getData ());
        }
        {
          final List <?> aElements = aJSON.getListProperty ("list"); //$NON-NLS-1$
          Assert.assertEquals (2, aElements.size ());
          Assert.assertEquals ("a", aElements.get (0)); //$NON-NLS-1$
          Assert.assertNull (aElements.get (1));
        }
      }
    }
    finally
    {
      JSONSettings.getInstance ().setParseNullValues (bOldValue);
    }
  }

  private static void testForParseException (final String sJSON)
  {
    try
    {
      JSONReader.parse (sJSON);
      Assert.fail ("should not have been paraseable: " + sJSON); //$NON-NLS-1$
    }
    catch (final JSONParsingException e)
    {
      // expected
    }
  }

  /**
   * Tests parsing a JSON string array into a property value list using the
   * {@link JSONReader}
   */
  @Test
  public void testParseArrayText () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[\"" + //$NON-NLS-1$
                                                                        STR_VALUE1 +
                                                                        "\", \"" + //$NON-NLS-1$
                                                                        STR_VALUE2 +
                                                                        "\", \"" + //$NON-NLS-1$
                                                                        STR_VALUE3 +
                                                                        "\"]"); //$NON-NLS-1$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 3);
    assertEquals (aJSONList.getValues ().get (0).getData (), STR_VALUE1);
    assertEquals (aJSONList.getValues ().get (1).getData (), STR_VALUE2);
    assertEquals (aJSONList.getValues ().get (2).getData (), STR_VALUE3);
  }

  /**
   * Tests parsing a JSON boolean array into a property value list using the
   * {@link JSONReader}
   */
  @Test
  public void testParseArrayBoolean () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[" + //$NON-NLS-1$
                                                                        Boolean.TRUE +
                                                                        ", " + //$NON-NLS-1$
                                                                        Boolean.FALSE +
                                                                        ", " + //$NON-NLS-1$
                                                                        Boolean.TRUE +
                                                                        "]"); //$NON-NLS-1$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 3);
    assertEquals (aJSONList.getValues ().get (0).getData (), Boolean.TRUE);
    assertEquals (aJSONList.getValues ().get (1).getData (), Boolean.FALSE);
    assertEquals (aJSONList.getValues ().get (2).getData (), Boolean.TRUE);
  }

  /**
   * Tests parsing a JSON integer array into a property value list using the
   * {@link JSONReader}
   */
  @Test
  public void testParseArrayInteger () throws Exception
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[" + //$NON-NLS-1$
                                                                        VALUE_INT1 +
                                                                        ", " + //$NON-NLS-1$
                                                                        VALUE_INT2 +
                                                                        ", " + //$NON-NLS-1$
                                                                        VALUE_INT3 +
                                                                        "]"); //$NON-NLS-1$
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
   * @throws JSONParsingException
   */
  @Test
  public void testParseArrayEmpty () throws JSONParsingException
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[]"); //$NON-NLS-1$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 0);
  }

  /**
   * Tests parsing an empty JSON array into a property value list using the
   * {@link JSONReader}
   * 
   * @throws JSONParsingException
   */
  @Test
  public void testParseArrayEmptyString () throws JSONParsingException
  {
    final IJSONPropertyValueList <?> aJSONList = JSONReader.parseArray ("[\"\"]"); //$NON-NLS-1$
    assertNotNull (aJSONList);
    assertEquals (aJSONList.getValues ().size (), 1);
  }

  /**
   * Tests parsing a JSON string value using the automated type detection
   * 
   * @throws JSONParsingException
   */
  @Test
  public void testAutoParseString () throws JSONParsingException
  {
    final IJSON aJSON = JSONReader.parse ("\"" + VALUE_ONE + "\""); //$NON-NLS-1$//$NON-NLS-2$
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof JSONPropertyValueString);
    assertEquals (((JSONPropertyValueString) aJSON).getData (), VALUE_ONE);
  }

  /**
   * Tests parsing a JSON integer value using the automated type detection
   * 
   * @throws JSONParsingException
   */
  @Test
  public void testAutoParseInteger () throws JSONParsingException
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
   * @throws JSONParsingException
   */
  @Test
  public void testAutoParseBoolean () throws JSONParsingException
  {
    final IJSON aJSON = JSONReader.parse (String.valueOf (VALUE_BOOL));
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof JSONPropertyValueBoolean);
    assertNotNull (((JSONPropertyValueBoolean) aJSON).getData ());
    assertTrue (((JSONPropertyValueBoolean) aJSON).getData ().booleanValue () == VALUE_BOOL);// NOPMD
  }

  /**
   * Tests parsing a JSON double value using the automated type detection
   * 
   * @throws JSONParsingException
   */
  @Test
  public void testAutoParseDouble () throws JSONParsingException
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
   * @throws JSONParsingException
   */
  @Test
  public void testAutoParseArray () throws JSONParsingException
  {
    final IJSON aJSON = JSONReader.parse ("[" + VALUE_INT1 + ", " + VALUE_INT2 + ", " + VALUE_INT3 + "]"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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
   * @throws JSONParsingException
   */
  @Test
  public void testAutoParseObject () throws JSONParsingException
  {
    final IJSON aJSON = JSONReader.parse (this.m_aComplexObject.getJSONString ());
    assertNotNull (aJSON);
    assertTrue (aJSON instanceof IJSONObject);
    assertEquals (aJSON, this.m_aComplexObject);
  }

  @Test
  public void testGetNumericFlexible () throws JSONParsingException
  {
    final IJSONObject aJSON = JSONReader.parseObject ("{\"" + PROPERTY + "\":2.345}"); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull (aJSON);
    final BigDecimal aBDVal = aJSON.getBigDecimalProperty (PROPERTY);
    assertNotNull (aBDVal);
    final Double aDoubleVal = aJSON.getDoubleProperty (PROPERTY);
    assertNotNull (aDoubleVal);
    assertEquals (aDoubleVal, Double.valueOf (aBDVal.doubleValue ()));

    final Integer aIntVal = aJSON.getIntegerProperty (PROPERTY);
    assertNotNull (aIntVal);
    assertEquals (aIntVal, Integer.valueOf (aDoubleVal.intValue ()));
  }

  @Test
  public void testBigDecimal ()
  {
    final JSONPropertyValueDouble aValDouble = new JSONPropertyValueDouble (0.1);
    final BigDecimal aBDVal1 = BigDecimal.valueOf (aValDouble.getData ().doubleValue ());
    final BigDecimal aBDVal2 = StringParser.parseBigDecimal (aValDouble.getData ().toString ());
    assertEquals (aBDVal1, aBDVal2);
  }

  @Test
  public void testParseMany () throws JSONParsingException
  {
    StringParser.parseBigInteger ("0E+39"); //$NON-NLS-1$

    final String sLongNum = "-123647126317982378123671523675123656128358162358712364712631798237812367152367512365612835816235871236471263179823781236715236751236561283581623587.1236471263179823781236715236751236561283581623587123647126317982378123671523675123656128358162358712364712631798237812367152367512365612835816235871236471263179823781236715236751236561283581623587"; //$NON-NLS-1$
    for (final String s : new String [] { "5", //$NON-NLS-1$
                                          "12345", //$NON-NLS-1$
                                          "-12345", //$NON-NLS-1$
                                          "1236471263179823781236715236751236561283581623587", //$NON-NLS-1$
                                          "-1236471263179823781236715236751236561283581623587", //$NON-NLS-1$
                                          "0", //$NON-NLS-1$
                                          "123647126317982378123671523675123656128358162358712364712631798237812367152367512365612835816235871236471263179823781236715236751236561283581623587.1236471263179823781236715236751236561283581623587123647126317982378123671523675123656128358162358712364712631798237812367152367512365612835816235871236471263179823781236715236751236561283581623587", //$NON-NLS-1$
                                          sLongNum,
                                          "[[4,5],[4,6],[4,7,8,9,10],\"abc\",[[4,7,8,9,10],[4,7,8,9,10],[4,7,8," + //$NON-NLS-1$
                                                    sLongNum +
                                                    ",9,10]," + //$NON-NLS-1$
                                                    sLongNum +
                                                    ",[4,7,8,9,10]]]" }) //$NON-NLS-1$
    {
      final IJSON aJSON = JSONReader.parse (s);
      assertNotNull ("Failed to parse " + s, aJSON); //$NON-NLS-1$
      assertEquals (s, aJSON.getJSONString (false)); // NOPMD
    }
  }

  @Test
  public void testParseLib () throws JSONParsingException
  {
    JSONReader.parse (StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_LIB),
                                                       CCharset.CHARSET_UTF_8_OBJ));
  }

  @Test
  public void testParseLibFull () throws JSONParsingException
  {
    JSONReader.parse (StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_LIB_FULL),
                                                       CCharset.CHARSET_UTF_8_OBJ));
  }

  @Test
  public void testCompareCloneLevels () throws JSONParsingException
  {
    testCloneLevel (true);
    testCloneLevel (false);
  }

  private void testCloneLevel (final boolean bClone) throws JSONParsingException
  {
    final boolean bOriginalState = JSONSettings.getInstance ().isCloneProperties ();
    JSONSettings.getInstance ().setCloneProperties (bClone);
    try
    {
      JSONStatistics.getInstance ().reset ();
      JSONStatistics.getInstance ().start ();
      JSONReader.parse (StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_LIB_FULL),
                                                         CCharset.CHARSET_UTF_8_OBJ));
      JSONStatistics.getInstance ().stop ();
      LOG.info ("OBJECTS: " + //$NON-NLS-1$
                JSONStatistics.getInstance ().getObjectCount () +
                " PROPERTIES: " + //$NON-NLS-1$
                JSONStatistics.getInstance ().getPropertyCount () +
                " VALUES: " + //$NON-NLS-1$
                JSONStatistics.getInstance ().getPropertyValueCount ());
    }
    finally
    {
      JSONSettings.getInstance ().setCloneProperties (bOriginalState);
    }
  }

  @Test
  public void testParseLibMini () throws JSONParsingException
  {
    JSONReader.parse (StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_LIB_MINI),
                                                       CCharset.CHARSET_UTF_8_OBJ));
  }

  @Test
  public void testParseModel () throws JSONParsingException
  {
    final IJSON aJSON = JSONReader.parse (StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_MODEL),
                                                                           CCharset.CHARSET_UTF_8_OBJ));
    Assert.assertTrue (aJSON instanceof IJSONObject);
    Assert.assertNotNull (((IJSONObject) aJSON).getStringProperty ("model")); //$NON-NLS-1$
  }

  @Test
  public void testEscaping () throws JSONParsingException
  {
    assertEscaped ("aaa\"bbb"); //$NON-NLS-1$
    assertEscaped ("aaa\\\"bbb"); //$NON-NLS-1$
    assertEscaped ("aaa\\/bbb"); //$NON-NLS-1$
    assertEscaped ("aaa\bbbb"); //$NON-NLS-1$
    assertEscaped (JSONHelperTest.getBadString ()); // $NON-NLS-1$
  }

  private static void assertEscaped (final String sValue) throws JSONParsingException
  {
    LOG.info ("value pure: {}", sValue); //$NON-NLS-1$
    final String sJSON = "{\"prop\":\"" + JSONHelper.jsonEscape (sValue) + "\"}"; //$NON-NLS-1$ //$NON-NLS-2$
    LOG.info ("JSON String: {}", sJSON); //$NON-NLS-1$
    final IJSONObject aJSON = JSONReader.parseObject (sJSON);
    Assert.assertEquals (sValue, aJSON.getStringProperty ("prop")); //$NON-NLS-1$

  }
}
