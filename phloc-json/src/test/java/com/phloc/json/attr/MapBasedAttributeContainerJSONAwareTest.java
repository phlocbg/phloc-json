package com.phloc.json.attr;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.JSONUtil;
import com.phloc.json.impl.JSONObject;
import com.phloc.json.impl.value.JSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueString;

/**
 * Test for {@link MapBasedAttributeContainerJSONAware}
 * 
 * @author Boris Gregorcic
 */
@SuppressWarnings ("static-method")
public class MapBasedAttributeContainerJSONAwareTest
{
  private static final String ATTR = "attr"; //$NON-NLS-1$

  @Test
  public void testGetJSONAttributeNull ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    Assert.assertNull (aMap.getJSONAttribute (ATTR, IJSONObject.class));
  }

  @Test
  public void testGetJSONAttributeAssignable ()
  {
    testGetAttributeAsJSONObject ();
  }

  @Test
  public void testGetJSONAttributeAsValidString ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    final IJSONObject aValue = new JSONObject ().setStringProperty (ATTR, ATTR);
    aMap.setAttribute (ATTR, aValue.getJSONString ());
    Assert.assertEquals (aValue, aMap.getJSONAttribute (ATTR, IJSONObject.class));
  }

  @Test
  public void testGetJSONAttributeAsInvalidString ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    aMap.setAttribute (ATTR, "{"); //$NON-NLS-1$
    Assert.assertNull (aMap.getJSONAttribute (ATTR, IJSONObject.class));
  }

  @Test
  public void testGetJSONAttributeAsInvalidType ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    aMap.setAttribute (ATTR, Integer.valueOf (42));
    Assert.assertNull (aMap.getJSONAttribute (ATTR, IJSONObject.class));
  }

  @Test
  public void testGetAttributeAsJSONObject ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    final IJSONObject aValue = new JSONObject ().setStringProperty (ATTR, ATTR);
    aMap.setAttribute (ATTR, aValue);
    Assert.assertEquals (aValue, aMap.getAttributeAsJSONObject (ATTR));
  }

  @Test
  public void testGetAttributeAsJSON ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    final IJSONObject aValue = new JSONObject ().setStringProperty (ATTR, ATTR);
    aMap.setAttribute (ATTR, aValue);
    Assert.assertEquals (aValue, aMap.getAttributeAsJSON (ATTR));
  }

  @Test
  public void testGetAttributeAsJSONArray ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    final IJSONPropertyValueList <?> aList = JSONUtil.getStringList (ContainerHelper.newList ("A", "B")); //$NON-NLS-1$ //$NON-NLS-2$
    aMap.setAttribute (ATTR, aList);
    Assert.assertEquals (aList, aMap.getAttributeAsJSONArray (ATTR));
  }

  @Test
  public void testGetAttributeAsJSONObjectArray ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    final List <IJSONObject> aDataList = ContainerHelper.newList ();
    aDataList.add (new JSONObject ().setStringProperty (ATTR, ATTR));
    final IJSONPropertyValueList <IJSONObject> aList = new JSONPropertyValueList <IJSONObject> ();
    aList.addAllValues (aDataList);
    aMap.setAttribute (ATTR, aList);
    Assert.assertEquals (aDataList, aMap.getAttributeAsJSONObjectArray (ATTR));
    Assert.assertNull (aMap.getAttributeAsJSONObjectArray ("foo")); //$NON-NLS-1$
  }

  @Test
  public void testGetAttributeAsJSONStringArray ()
  {
    final MapBasedAttributeContainerJSONAware aMap = new MapBasedAttributeContainerJSONAware ();
    final List <String> aDataList = ContainerHelper.newList ();
    aDataList.add ("A"); //$NON-NLS-1$
    aDataList.add ("B"); //$NON-NLS-1$
    final IJSONPropertyValueList <IJSONPropertyValue <String>> aList = new JSONPropertyValueList <IJSONPropertyValue <String>> ();
    for (final String sDataValue : aDataList)
    {
      aList.addValue (new JSONPropertyValueString (sDataValue));
    }
    aMap.setAttribute (ATTR, aList);
    Assert.assertEquals (aDataList, aMap.getAttributeAsJSONStringArray (ATTR));
    Assert.assertNull (aMap.getAttributeAsJSONStringArray ("foo")); //$NON-NLS-1$
  }

}
