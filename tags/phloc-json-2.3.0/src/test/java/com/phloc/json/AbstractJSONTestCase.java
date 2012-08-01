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
package com.phloc.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.json.impl.JSONObject;
import com.phloc.json.impl.JSONReader;
import com.phloc.json.impl.value.JSONPropertyValueList;

public abstract class AbstractJSONTestCase
{
  protected static final String PROP_ONE = "ONE";
  protected static final String PROP_TWO = "TWO";
  protected static final String PROP_THREE = "THREE";
  protected static final String PROP_FOUR = "FOUR";
  protected static final String PROP_FIVE = "FIVE";

  protected static final String VALUE_ONE = "dummy";
  protected static final String VALUE_TWO = "1234";
  protected static final String VALUE_THREE = "0815";
  protected static final boolean VALUE_BOOL = true;
  protected static final int VALUE_INT1 = 42;
  protected static final int VALUE_INT2 = 47;
  protected static final int VALUE_INT3 = 11;
  protected static final double VALUE_DOUBLE = 1.78;
  protected static final String VALUE_FOUR = "I\"1";
  protected static final String VALUE_FIVE = "I\t2";

  protected static final String STR_VALUE1 = "puit";
  protected static final String STR_VALUE2 = "fjord";
  protected static final String STR_VALUE3 = "narf";

  protected final IJSONObject m_aSimpleObject;
  protected final IJSONObject m_aComplexObject;

  @Nonnull
  private static final IJSONObject _createTestObjectSimple ()
  {
    final JSONObject aObj = new JSONObject ();
    aObj.setIntegerProperty (PROP_ONE, VALUE_INT1);
    aObj.setBooleanProperty (PROP_TWO, VALUE_BOOL);
    final List <String> aListValues = ContainerHelper.newList (VALUE_TWO, VALUE_THREE);
    aObj.setStringListProperty (PROP_THREE, aListValues);
    return aObj;
  }

  @Nonnull
  private static final IJSONObject _createTestObjectComplex ()
  {
    final IJSONObject aObj = _createTestObjectSimple ();
    aObj.setObjectProperty (PROP_FOUR, _createTestObjectSimple ());
    final IJSONPropertyValueList <IJSONPropertyValueList <IJSONObject>> aList = new JSONPropertyValueList <IJSONPropertyValueList <IJSONObject>> ();

    final JSONPropertyValueList <IJSONObject> aInnerListOne = new JSONPropertyValueList <IJSONObject> ();
    aInnerListOne.addValue (_createTestObjectSimple ());
    aInnerListOne.addValue (_createTestObjectSimple ());
    aList.addValue (aInnerListOne);

    final JSONPropertyValueList <IJSONObject> aInnerListTwo = new JSONPropertyValueList <IJSONObject> ();
    aInnerListTwo.addValue (_createTestObjectSimple ());
    aInnerListTwo.addValue (_createTestObjectSimple ());
    aList.addValue (aInnerListTwo);

    aObj.setListProperty (PROP_FIVE, aList);
    return aObj;
  }

  /**
   * Ctor
   */
  protected AbstractJSONTestCase ()
  {
    m_aSimpleObject = _createTestObjectSimple ();
    m_aComplexObject = _createTestObjectComplex ();
  }

  @Test
  public final void testGetJSONString ()
  {
    final IJSONObject aObj = _createTestObjectComplex ();

    final String sPrettyJSON = aObj.getJSONString (true);
    final String sCompactJSON = aObj.getJSONString ();

    final IJSONObject aPrettyParsed = JSONReader.parseObject (sPrettyJSON);
    final IJSONObject aCompactParsed = JSONReader.parseObject (sCompactJSON);

    assertEquals (aPrettyParsed, aObj);
    assertEquals (aCompactParsed, aObj);
  }

  @Test
  public final void doTestJSONObject ()
  {
    assertNotNull (m_aSimpleObject.getProperty (PROP_ONE));
    assertNotNull (m_aSimpleObject.getProperty (PROP_TWO));
    assertNotNull (m_aSimpleObject.getProperty (PROP_THREE));
    assertEquals (m_aSimpleObject.getIntegerProperty (PROP_ONE), Integer.valueOf (VALUE_INT1));
    assertEquals (m_aSimpleObject.getBooleanProperty (PROP_TWO), Boolean.TRUE);
    assertEquals (m_aSimpleObject.getProperty (PROP_ONE).getValue ().getData (), Integer.valueOf (VALUE_INT1));
    assertEquals (m_aSimpleObject.getProperty (PROP_TWO).getValue ().getData (), Boolean.valueOf (VALUE_BOOL));

    final List <?> aListValues = m_aSimpleObject.getListProperty (PROP_THREE);
    assertEquals (aListValues.size (), 2);
    assertEquals (aListValues.get (0), VALUE_TWO);
    assertEquals (aListValues.get (1), VALUE_THREE);
  }

  @Test
  public final void doTestClone ()
  {
    PhlocTestUtils.testGetClone (m_aSimpleObject);
    PhlocTestUtils.testGetClone (m_aComplexObject);
  }
}
