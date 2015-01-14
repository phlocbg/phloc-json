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
package com.phloc.json.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueString;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link JSONObject}
 * 
 * @author Philip Helger
 * @author Boris Gregorcic
 */
@SuppressWarnings ("static-method")
public final class JSONObjectTest
{
  private static final String PROP = "property"; //$NON-NLS-1$
  private static final String KEY = "testProperty"; //$NON-NLS-1$
  private static final String A_KEY = "A"; //$NON-NLS-1$
  private static final String B_KEY = "B"; //$NON-NLS-1$
  private static final String C_KEY = "C"; //$NON-NLS-1$
  private static final String A_VAL = "AAAA"; //$NON-NLS-1$
  private static final String B_VAL = "BBBB"; //$NON-NLS-1$
  private static final Integer C_VAL = Integer.valueOf (5);
  private static final Boolean D_VAL = Boolean.TRUE;
  private static final IJSONObject E_VAL = new JSONObject ().setStringProperty (A_KEY, A_VAL);

  @Test
  public void testSimple () throws JSONParsingException
  {
    JSONObject aObj = new JSONObject ();
    aObj.setStringProperty ("a", "b"); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertEquals ("{\"a\":\"b\"}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setBigDecimalProperty ("x", new BigDecimal ("1514131211.0987654321")); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertEquals ("{\"x\":1514131211.0987654321}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setBigIntegerProperty ("a", new BigInteger ("1514131288877788877711")); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertEquals ("{\"a\":1514131288877788877711}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setBooleanProperty ("a", false); //$NON-NLS-1$
    Assert.assertEquals ("{\"a\":false}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setDoubleProperty ("a", 123.45); //$NON-NLS-1$
    Assert.assertEquals ("{\"a\":123.45}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    // Parsed object will be BigDecimal!
    // Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString
    // ()));

    aObj = new JSONObject ();
    aObj.setFunctionProperty ("a", "alert;", "foo", "bar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    Assert.assertEquals ("{\"a\":function(foo,bar){alert;}}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertTrue (aObj.containsNotParsableProperty ());

    // Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString
    // ()));

    aObj = new JSONObject ();
    aObj.setFunctionPrebuildProperty ("a", " function (page) { alert (page + \" of 17\"); } "); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertEquals ("{\"a\": function (page) { alert (page + \" of 17\"); } }", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertTrue (aObj.containsNotParsableProperty ());
    // Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString
    // ()));

    aObj = new JSONObject ();
    aObj.setIntegerProperty ("a", 15); //$NON-NLS-1$
    Assert.assertEquals ("{\"a\":15}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setKeywordProperty ("a", "anything"); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertEquals ("{\"a\":anything}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertTrue (aObj.containsNotParsableProperty ());
    // Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString
    // ()));

    aObj = new JSONObject ();
    aObj.setLongProperty ("a", 12345678901L); //$NON-NLS-1$
    Assert.assertEquals ("{\"a\":12345678901}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString ()));

    aObj = new JSONObject ();
    aObj.setDoubleProperty ("abc", 15.34); //$NON-NLS-1$
    Assert.assertEquals ("{\"abc\":15.34}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertEquals (Double.valueOf (15.34), aObj.getDoubleProperty ("abc")); //$NON-NLS-1$
    aObj.setDoubleProperty ("def", Double.valueOf (16.79)); //$NON-NLS-1$
    Assert.assertEquals ("{\"abc\":15.34,\"def\":16.79}", aObj.getJSONString ()); //$NON-NLS-1$
    Assert.assertEquals (Double.valueOf (15.34), aObj.getDoubleProperty ("abc")); //$NON-NLS-1$
    Assert.assertEquals (Double.valueOf (16.79), aObj.getDoubleProperty ("def")); //$NON-NLS-1$
    Assert.assertFalse (aObj.containsNotParsableProperty ());
    // Will be parsed as BigDecimal
    // Assert.assertEquals (aObj, JSONReader.parseObject (aObj.getJSONString
    // ()));
  }

  /**
   * Test for passing a null JSON object
   */

  @Test (expected = NullPointerException.class)
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testCreateJSONObjectNull ()
  {
    new JSONObject (null);
  }

  @Test (expected = NullPointerException.class)
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testSetPropertyNull ()
  {
    new JSONObject ().setProperty (null);
  }

  @Test
  public void testSetProperty ()
  {
    final IJSONObject aObj = new JSONObject ();
    final boolean bUseTypeConverter = false;
    final String sKey = "testProperty"; //$NON-NLS-1$
    final String sTestString = "testString"; //$NON-NLS-1$

    {
      // set null
      final Object aValue = sTestString;
      aObj.setProperty (sKey, aValue, bUseTypeConverter);
      aObj.setProperty (sKey, null, bUseTypeConverter);
      Assert.assertNull (aObj.getProperty (sKey));
    }

    {
      // set string property
      checkSetProperty (sTestString);
    }

    {
      // set JSON object property
      checkSetProperty (new JSONObject ().setStringProperty (sKey, sTestString));
    }

    {
      // set JSON property value (JSON object)
      checkSetProperty (new JSONPropertyValueInteger (Integer.valueOf (Integer.MIN_VALUE)), true, false);
      checkSetProperty (new JSONPropertyValueInteger (Integer.valueOf (Integer.MAX_VALUE)), true, false);
    }

    {
      // set boolean property
      checkSetProperty (Boolean.FALSE);
      checkSetProperty (Boolean.TRUE);
    }

    {
      // set boolean property
      checkSetProperty (BigInteger.valueOf (Long.MIN_VALUE));
      checkSetProperty (BigInteger.valueOf (Long.MAX_VALUE));
    }

    {
      // set boolean property
      checkSetProperty (BigDecimal.valueOf (Double.MIN_VALUE));
      checkSetProperty (BigDecimal.valueOf (Double.MAX_VALUE));
    }

    {
      // set boolean property
      checkSetProperty (Double.valueOf (Double.MIN_VALUE));
      checkSetProperty (Double.valueOf (Double.MAX_VALUE));
    }

    {
      // set boolean property
      checkSetProperty (Integer.valueOf (Integer.MIN_VALUE));
      checkSetProperty (Integer.valueOf (Integer.MAX_VALUE));
    }

    {
      // set boolean property
      checkSetProperty (Long.valueOf (Long.MIN_VALUE));
      checkSetProperty (Long.valueOf (Long.MAX_VALUE));
    }

    {
      // set with type converter
      checkSetProperty (new Date (), false, true);
    }

    {
      // set without type converter
      final Object aValue = new Date ();
      aObj.setProperty (KEY, aValue, bUseTypeConverter);
      Assert.assertEquals (aValue.toString (), aObj.getProperty (KEY).getValue ().getData ());
    }
  }

  @Test
  public void testApply ()
  {
    final IJSONObject aSource = new JSONObject ();

    aSource.setStringProperty (A_KEY, A_VAL);
    aSource.setIntegerProperty (C_KEY, C_VAL);

    final IJSONObject aTarget = new JSONObject ();
    aTarget.apply (null);
    Assert.assertTrue (aTarget.isEmpty ());

    aTarget.setStringProperty (B_KEY, B_VAL);
    aTarget.apply (aSource);
    Assert.assertEquals (aTarget.getStringProperty (A_KEY), A_VAL);
    Assert.assertEquals (aTarget.getStringProperty (B_KEY), B_VAL);
    Assert.assertEquals (aTarget.getIntegerProperty (C_KEY), C_VAL);
  }

  @Test
  public void testApplyProperty ()
  {
    final IJSONObject aSource = new JSONObject ();

    aSource.setStringProperty (A_KEY, A_VAL);
    aSource.setIntegerProperty (C_KEY, C_VAL);

    final IJSONObject aTarget = new JSONObject ();
    aTarget.apply (null, A_KEY);
    Assert.assertTrue (aTarget.isEmpty ());

    aTarget.setStringProperty (A_KEY, B_VAL);
    aTarget.apply (aSource, A_KEY);
    Assert.assertEquals (aTarget.getPropertyCount (), 1);
    Assert.assertEquals (aTarget.getStringProperty (A_KEY), A_VAL);
    aTarget.apply (aSource, "invalid"); //$NON-NLS-1$
    Assert.assertEquals (aTarget.getPropertyCount (), 1);
  }

  @Test
  public void testGetPropertyValueData ()
  {
    final IJSONObject aObj = new JSONObject ();
    aObj.setStringProperty (A_KEY, A_VAL);
    Assert.assertNull (aObj.getPropertyValueData (B_KEY));
    Assert.assertEquals (aObj.getPropertyValueData (A_KEY), A_VAL);
  }

  @Test
  public void testRemoveProperty ()
  {
    final IJSONObject aObj = new JSONObject ();
    aObj.setStringProperty (A_KEY, A_VAL);
    Assert.assertEquals (EChange.CHANGED, aObj.removeProperty (A_KEY));
    Assert.assertNull (aObj.getProperty (A_KEY));
    Assert.assertEquals (EChange.UNCHANGED, aObj.removeProperty (A_KEY));
  }

  @Test
  public void testSetMixedListProperty ()
  {
    final IJSONObject aObj = new JSONObject ();
    final List <Object> aValues = ContainerHelper.newList ();
    aValues.add (A_VAL);
    aValues.add (C_VAL);
    aValues.add (D_VAL);
    aValues.add (E_VAL);
    aObj.setMixedListProperty (A_KEY, aValues);
    Assert.assertEquals (aValues, aObj.getListProperty (A_KEY));
  }

  @Test
  public void testGetNumericProperty ()
  {
    checkNumericProperty (Integer.valueOf (Integer.MAX_VALUE));
    checkNumericProperty (BigDecimal.valueOf (Double.MAX_VALUE));
    checkNumericProperty (Double.valueOf (Double.MAX_VALUE));
    checkNumericProperty (BigInteger.valueOf (Long.MAX_VALUE));
    checkNumericProperty (Long.valueOf (Long.MAX_VALUE));

    // error cases
    Assert.assertNull (new JSONObject ().getNumericProperty (A_KEY));
    Assert.assertNull (new JSONObject ().setStringProperty (A_KEY, A_VAL).getNumericProperty (A_KEY));
  }

  private void checkNumericProperty (final Number aValue)
  {
    final IJSONObject aObj = new JSONObject ();
    aObj.setProperty (A_KEY, aValue);
    Assert.assertEquals (aValue, aObj.getNumericProperty (A_KEY));
  }

  private static void checkSetProperty (final Object aValue)
  {
    checkSetProperty (aValue, false, false);
  }

  private static void checkSetProperty (final Object aValue,
                                        final boolean bCompareJSONValue,
                                        final boolean bUseTypeConverter)
  {
    final IJSONObject aObj = new JSONObject ();
    if (bUseTypeConverter)
    {
      aObj.setProperty (KEY, aValue, bUseTypeConverter);
    }
    else
    {
      aObj.setProperty (KEY, aValue);
    }

    if (!bCompareJSONValue && bUseTypeConverter)
    {
      Assert.assertEquals (TypeConverter.convertIfNecessary (aValue, String.class), aObj.getProperty (KEY)
                                                                                        .getValue ()
                                                                                        .getData ());
    }
    else
    {
      Assert.assertEquals (aValue, bCompareJSONValue ? aObj.getProperty (KEY).getValue () : aObj.getProperty (KEY)
                                                                                                .getValue ()
                                                                                                .getData ());
    }
  }

  /**
   * Test for {@link JSONObject#getIntegerPropertyNonNull(String)} passing an
   * invalid property name
   */
  @Test (expected = NullPointerException.class)
  public void testGetIntegerPropertyNonNullNullParam ()
  {
    new JSONObject ().getIntegerPropertyNonNull (""); //$NON-NLS-1$
  }

  /**
   * Test for {@link JSONObject#getIntegerPropertyNonNull(String)} when property
   * is not existing
   */
  @Test (expected = NullPointerException.class)
  public void testGetIntegerPropertyNonNullInexistent ()
  {
    new JSONObject ().getIntegerPropertyNonNull (PROP);
  }

  /**
   * Test for {@link JSONObject#getIntegerPropertyNonNull(String)} success case
   */
  @Test
  public void testGetIntegerPropertyNonNull ()
  {
    final IJSONObject aObject = new JSONObject ();
    aObject.setIntegerProperty (PROP, 5);
    Assert.assertEquals (aObject.getIntegerPropertyNonNull (PROP).intValue (), 5);
  }

  /**
   * Test for {@link JSONObject#getStringPropertyNonEmpty(String)} passing an
   * invalid property name
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetStringPropertyNonEmptyNullParam ()
  {
    new JSONObject ().getStringPropertyNonEmpty (""); //$NON-NLS-1$
  }

  /**
   * Test for {@link JSONObject#getStringPropertyNonEmpty(String)} when property
   * is not existing
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetStringPropertyNonEmptyInexistent ()
  {
    new JSONObject ().getStringPropertyNonEmpty (PROP);
  }

  /**
   * Test for {@link JSONObject#getStringPropertyNonEmpty(String)} success case
   */
  @Test
  public void testGetStringPropertyNonEmpty ()
  {
    final IJSONObject aObject = new JSONObject ();
    aObject.setStringProperty (PROP, "bla"); //$NON-NLS-1$
    Assert.assertEquals (aObject.getStringPropertyNonEmpty (PROP), "bla"); //$NON-NLS-1$
  }

  /**
   * Test for {@link JSONObject#getObjectListProperty(String)} success case
   */
  @Test
  public void setGetObjectListProperty ()
  {
    final List <IJSONObject> aJSONObjects = ContainerHelper.newList ();
    aJSONObjects.add (new JSONObject ().setStringProperty (A_KEY, A_VAL));
    final IJSONObject aObject = new JSONObject ();
    aObject.setObjectListProperty (KEY, aJSONObjects);
    Assert.assertTrue (aObject.getObjectListProperty (PROP).isEmpty ());
    Assert.assertEquals (aObject.getObjectListProperty (KEY), aJSONObjects);
  }

  @Test
  public void testSetListOfStringListProperty ()
  {
    final JSONObject aObject = new JSONObject ();
    final List <List <String>> aListOfList = ContainerHelper.newList ();
    final List <String> aListA = ContainerHelper.newList ();
    aListA.add ("A1"); //$NON-NLS-1$
    aListA.add ("A2"); //$NON-NLS-1$
    aListOfList.add (aListA);
    final List <String> aListB = ContainerHelper.newList ();
    aListB.add ("B1"); //$NON-NLS-1$
    aListB.add ("B2"); //$NON-NLS-1$
    aListOfList.add (aListB);

    aObject.setListOfStringListProperty (PROP, aListOfList);
    final List <List <JSONPropertyValueString>> aValue = aObject.getListProperty (PROP);
    Assert.assertEquals (2, aValue.size ());
    Assert.assertEquals (2, aValue.get (0).size ());
    Assert.assertEquals (2, aValue.get (1).size ());
    Assert.assertEquals ("A1", aValue.get (0).get (0).getData ()); //$NON-NLS-1$
  }

  @Test
  public void testSetIntegerListProperty ()
  {
    final int [] aValues = { 1, 4, 7 };
    final IJSONObject aObject = new JSONObject ();
    aObject.setIntegerListProperty (PROP, aValues);
    final List <Integer> aRetrievedValues = aObject.getListProperty (PROP);
    Assert.assertEquals (Integer.valueOf (1), aRetrievedValues.get (0));
    Assert.assertEquals (Integer.valueOf (4), aRetrievedValues.get (1));
    Assert.assertEquals (Integer.valueOf (7), aRetrievedValues.get (2));
  }

  @Test
  public void testSetIntegerListPropertyByList ()
  {
    final List <Integer> aValues = ContainerHelper.newList (Integer.valueOf (1),
                                                            Integer.valueOf (4),
                                                            Integer.valueOf (7));
    final IJSONObject aObject = new JSONObject ();
    aObject.setIntegerListProperty (PROP, aValues);
    final List <Integer> aRetrievedValues = aObject.getListProperty (PROP);
    Assert.assertEquals (aValues, aRetrievedValues);
  }

  @Test
  public void testGetPropertyCount ()
  {
    final IJSONObject aObject = new JSONObject ();
    Assert.assertEquals (0, aObject.getPropertyCount ());
    aObject.setStringProperty (PROP, "asdasd"); //$NON-NLS-1$
    Assert.assertEquals (1, aObject.getPropertyCount ());
    aObject.setIntegerProperty (PROP, 6);
    Assert.assertEquals (1, aObject.getPropertyCount ());
    aObject.setStringProperty ("asd", "asdasd"); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertEquals (2, aObject.getPropertyCount ());
    aObject.removeProperty ("asd"); //$NON-NLS-1$
    Assert.assertEquals (1, aObject.getPropertyCount ());
  }

  @Test
  public void testSetStringListProperty ()
  {
    final IJSONObject aObject = new JSONObject ();
    final List <String> aList = ContainerHelper.newList ("A", "B"); //$NON-NLS-1$ //$NON-NLS-2$
    aObject.setStringListProperty (PROP, aList);
    Assert.assertEquals (aList, aObject.getListProperty (PROP));
  }

  @Test
  public void testGetListValues ()
  {
    final IJSONObject aObject = new JSONObject ();
    final List <String> aList = ContainerHelper.newList ("A", "B"); //$NON-NLS-1$ //$NON-NLS-2$
    aObject.setStringListProperty (PROP, aList);
    final List <? extends IJSONPropertyValue <?>> aRetrieved = aObject.getListValues (PROP);
    Assert.assertEquals (2, aRetrieved.size ());
    Assert.assertEquals ("A", aRetrieved.get (0).getData ()); //$NON-NLS-1$
    Assert.assertEquals ("B", aRetrieved.get (1).getData ()); //$NON-NLS-1$
    Assert.assertNull (new JSONObject ().getListValues (PROP));
  }

  @Test
  public void testGetBigDecimalProperty ()
  {
    final JSONObject aObject = new JSONObject ();
    Assert.assertNull (aObject.getBigDecimalProperty (PROP));
    aObject.setBigDecimalProperty (PROP, BigDecimal.valueOf (47.11));
    Assert.assertEquals (BigDecimal.valueOf (47.11), aObject.getBigDecimalProperty (PROP));
    aObject.setDoubleProperty (PROP, Double.valueOf (47.11));
    Assert.assertEquals (BigDecimal.valueOf (47.11), aObject.getBigDecimalProperty (PROP));
    aObject.setIntegerProperty (PROP, Integer.valueOf (47));
    Assert.assertEquals (BigDecimal.valueOf (47.0), aObject.getBigDecimalProperty (PROP));
    aObject.setBigIntegerProperty (PROP, BigInteger.valueOf (47));
    Assert.assertEquals (BigDecimal.valueOf (47.0), aObject.getBigDecimalProperty (PROP));
    aObject.setLongProperty (PROP, Long.valueOf (47));
    Assert.assertEquals (BigDecimal.valueOf (47.0), aObject.getBigDecimalProperty (PROP));
  }
}
