package com.phloc.json.impl;

import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.text.IMultiLingualText;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.commons.url.SMap;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.JSONUtil;
import com.phloc.json.impl.value.JSONPropertyValueList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test for {@link JSONUtil}
 * 
 * @author Boris Gregorcic
 */
@SuppressWarnings ("static-method")
public class JSONUtilTest
{
  private static final String PROP = "property"; //$NON-NLS-1$
  private static final String VALUE1 = "value1"; //$NON-NLS-1$
  private static final String VALUE2 = "value2"; //$NON-NLS-1$

  /**
   * Test for {@link JSONUtil#getStringList(List)}
   * 
   * @throws JSONParsingException
   */
  @Test
  public void testGetStringList () throws JSONParsingException
  {
    final List <String> aValues = ContainerHelper.newList ("a", "b", "c", "a"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    final String sJSON = JSONUtil.getStringList (aValues).getJSONString ();
    final IJSONPropertyValueList <?> aParsedJSON = JSONReader.parseArray (sJSON);
    Assert.assertEquals (aParsedJSON.getDataValues (), aValues);
  }

  /**
   * Test for
   * {@link JSONUtil#getMultiLingualText(com.phloc.json.IJSONObject, String)}
   * passing a null JSON object
   */

  @Test (expected = NullPointerException.class)
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testGetMultiLingualTextNullJSON ()
  {
    JSONUtil.getMultiLingualTextProperty (null, PROP);
  }

  /**
   * Test for
   * {@link JSONUtil#getMultiLingualText(com.phloc.json.IJSONObject, String)}
   * passing a null attribute name
   */
  @Test (expected = IllegalArgumentException.class)
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testGetMultiLingualTextNullAttr ()
  {
    JSONUtil.getMultiLingualTextProperty (new JSONObject (), null);
  }

  /**
   * Test for
   * {@link JSONUtil#getMultiLingualText(com.phloc.json.IJSONObject, String)}
   * passing a JSON data not containing the specified object property
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetMultiLingualTextInvalidData ()
  {
    JSONUtil.getMultiLingualTextProperty (new JSONObject (), PROP);
  }

  /**
   * Test for
   * {@link JSONUtil#getMultiLingualText(com.phloc.json.IJSONObject, String)}
   */
  @Test
  public void testGetMultiLingualText ()
  {
    final JSONObject aData = new JSONObject ();
    final IJSONObject aMLText = new JSONObject ();
    aMLText.setStringProperty (Locale.GERMAN.toString (), VALUE1);
    aMLText.setStringProperty (Locale.ENGLISH.toString (), VALUE2);
    aData.setObjectProperty (PROP, aMLText);
    final IMultiLingualText aText = JSONUtil.getMultiLingualTextProperty (aData, PROP);
    Assert.assertNotNull (aText);
    Assert.assertEquals (aText.getAllLocales ().size (), 2);
    Assert.assertEquals (aText.getText (Locale.GERMAN), VALUE1);
    Assert.assertEquals (aText.getText (Locale.ENGLISH), VALUE2);
  }

  /**
   * Test for {@link JSONUtil#isNullOrEmpty(IJSONObject)}
   */
  @Test
  public void testIsNullOrEmpty ()
  {
    Assert.assertTrue (JSONUtil.isNullOrEmpty (null));
    final JSONObject aObject = new JSONObject ();
    Assert.assertTrue (JSONUtil.isNullOrEmpty (aObject));
    aObject.setStringProperty (PROP, VALUE1);
    Assert.assertFalse (JSONUtil.isNullOrEmpty (aObject));
  }

  /**
   * Test for {@link JSONUtil#parseStringArray(String)} passing null
   */
  @Test
  public void testParseStringArrayEmpty ()
  {
    JSONUtil.parseStringArray (null);
  }

  /**
   * Test for {@link JSONUtil#parseStringArray(String)} passing an invalid JSON
   * syntax
   */
  @Test
  public void testParseStringArrayInvalidSyntax ()
  {
    Assert.assertNull (JSONUtil.parseStringArray ("<no JSON}")); //$NON-NLS-1$
  }

  /**
   * Test for {@link JSONUtil#parseObjectArray(String)} passing a JSON array of
   * wrong data type
   */
  @Test
  public void testParseObjectArray ()
  {
    final List <IJSONObject> aObjects = JSONUtil.parseObjectArray ("[{\"a\":1},{\"a\":2}]"); //$NON-NLS-1$
    Assert.assertNotNull (aObjects);
    Assert.assertEquals (aObjects.size (), 2);
  }

  /**
   * Test for {@link JSONUtil#parseObjectArray(String)} passing a JSON array of
   * wrong data type
   */
  @Test
  public void testParseObjectArrayInvalidType ()
  {
    Assert.assertNull (JSONUtil.parseObjectArray ("[1,3,6]")); //$NON-NLS-1$
  }

  /**
   * Test for {@link JSONUtil#parseStringArray(String)} passing a JSON array of
   * wrong data type
   */
  @Test
  public void testParseStringArrayInvalidType ()
  {
    Assert.assertNull (JSONUtil.parseStringArray ("[1,3,6]")); //$NON-NLS-1$
  }

  /**
   * Test for {@link JSONUtil#parseStringArray(String)} passing null
   */
  @Test
  public void testParseStringArray ()
  {
    final List <String> aValues = JSONUtil.parseStringArray ("[\"A\",\"B\",\"C\"]"); //$NON-NLS-1$
    Assert.assertEquals (aValues.size (), 3);
    Assert.assertTrue (aValues.contains ("A")); //$NON-NLS-1$
    Assert.assertTrue (aValues.contains ("B")); //$NON-NLS-1$
    Assert.assertTrue (aValues.contains ("C")); //$NON-NLS-1$
  }

  /**
   * Test for
   * {@link JSONUtil#getMultiLingualTextAsJSON(com.phloc.commons.text.IMultiLingualText)}
   */
  @Test
  public void testGetMultiLingualTextAsJSON ()
  {
    Assert.assertEquals (JSONUtil.getMultiLingualTextAsJSON (null), new JSONObject ());

    final IMultiLingualText aText = MultiLingualText.createFromMap (new SMap ().add (Locale.GERMAN.toString (), VALUE1)
                                                                               .add (Locale.ENGLISH.toString (), VALUE2));
    final IJSONObject aJSON = JSONUtil.getMultiLingualTextAsJSON (aText);
    Assert.assertNotNull (aJSON);
    Assert.assertEquals (aJSON.getStringProperty (Locale.GERMAN.toString ()), VALUE1);
    Assert.assertEquals (aJSON.getStringProperty (Locale.ENGLISH.toString ()), VALUE2);
  }

  /**
   * Test for
   * {@link JSONUtil#getJSONMultiArray(com.phloc.json.IJSONConvertible[][])}
   * success case
   */
  @Test
  public void testGetJSONMultiArray ()
  {
    final IJSONObject aObject = new JSONObject ();
    final MockJSONConvertible [][] aValues = new MockJSONConvertible [2] [3];
    aValues[0][0] = new MockJSONConvertible ("11"); //$NON-NLS-1$
    aValues[0][1] = new MockJSONConvertible ("12"); //$NON-NLS-1$
    aValues[0][2] = new MockJSONConvertible ("13"); //$NON-NLS-1$
    aValues[1][0] = new MockJSONConvertible ("21"); //$NON-NLS-1$
    final MockJSONConvertible aCell22 = new MockJSONConvertible ("22"); //$NON-NLS-1$
    aValues[1][1] = aCell22;
    aValues[1][2] = new MockJSONConvertible ("23"); //$NON-NLS-1$
    final JSONPropertyValueList <IJSONPropertyValueList <IJSONObject>> aData = JSONUtil.getJSONMultiArray (aValues);
    Assert.assertEquals (aData.getValues ().size (), 2);
    Assert.assertEquals (aData.getValues ().get (0).getValues ().size (), 3);
    Assert.assertEquals (aData.getValues ().get (1).getValues ().get (1).getJSONString (), aCell22.getAsJSON ()
                                                                                                  .getJSONString ());
    // also testing adding the property
    aObject.setProperty (PROP, aData);
    Assert.assertNotNull (aObject.getProperty (PROP));
  }
}
