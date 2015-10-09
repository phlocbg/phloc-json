package com.phloc.json.attr;

import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.JSONUtil;
import com.phloc.json.impl.JSONParsingException;
import com.phloc.json.impl.JSONReader;

public class MapBasedAttributeContainerJSONAware extends MapBasedAttributeContainer
{
  private static final long serialVersionUID = -3784152596943504187L;
  private static final Logger LOG = LoggerFactory.getLogger (MapBasedAttributeContainerJSONAware.class);

  @SuppressWarnings ("unchecked")
  @Nullable
  protected <T extends IJSON> T getJSONAttribute (final String sParameterName, final Class <T> aClass)
  {
    final Object aAttributeValue = getAttributeObject (sParameterName);
    if (aAttributeValue == null)
    {
      return null;
    }
    if (aClass.isAssignableFrom (aAttributeValue.getClass ()))
    {
      return (T) aAttributeValue;
    }
    if (aAttributeValue instanceof String)
    {
      try
      {
        return (T) JSONReader.parse ((String) aAttributeValue);
      }
      catch (final JSONParsingException aEx)
      {
        LOG.error ("Unable to parse the attribute '" + sParameterName + "' to JSON!", aEx); //$NON-NLS-1$ //$NON-NLS-2$
        return null;
      }
    }
    return null;
  }

  @Nullable
  public IJSON getAttributeAsJSON (final String sParameterName)
  {
    return getJSONAttribute (sParameterName, IJSON.class);
  }

  @Nullable
  public IJSONObject getAttributeAsJSONObject (final String sParameterName)
  {
    return getJSONAttribute (sParameterName, IJSONObject.class);
  }

  @Nullable
  public IJSONPropertyValueList <?> getAttributeAsJSONArray (final String sParameterName)
  {
    return getJSONAttribute (sParameterName, IJSONPropertyValueList.class);
  }

  @Nullable
  public List <IJSONObject> getAttributeAsJSONObjectArray (final String sParameterName)
  {
    final IJSONPropertyValueList <?> aValues = getAttributeAsJSONArray (sParameterName);
    if (aValues != null)
    {
      return JSONUtil.getArrayAsElementList (aValues, IJSONObject.class);
    }
    return null;
  }

  @Nullable
  public List <String> getAttributeAsJSONStringArray (final String sParameterName)
  {
    final IJSONPropertyValueList <?> aValues = getAttributeAsJSONArray (sParameterName);
    if (aValues != null)
    {
      return JSONUtil.getArrayAsElementList (aValues, String.class);
    }
    return null;
  }
}
