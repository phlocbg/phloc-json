package com.phloc.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IMultiLingualText;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.json.impl.JSONObject;
import com.phloc.json.impl.JSONReader;
import com.phloc.json.impl.value.JSONPropertyValueBigDecimal;
import com.phloc.json.impl.value.JSONPropertyValueBigInteger;
import com.phloc.json.impl.value.JSONPropertyValueBoolean;
import com.phloc.json.impl.value.JSONPropertyValueDouble;
import com.phloc.json.impl.value.JSONPropertyValueInteger;
import com.phloc.json.impl.value.JSONPropertyValueList;
import com.phloc.json.impl.value.JSONPropertyValueLong;
import com.phloc.json.impl.value.JSONPropertyValueString;

/**
 * @author Hanno Fallmann
 */
public final class JSONUtil
{

  private static final Logger LOG = LoggerFactory.getLogger (JSONUtil.class);

  private JSONUtil ()
  {
    // private
  }

  /**
   * Converts the passed multi lingual test to a JSON object
   * 
   * @param aText
   *        The text, may be <code>null</code>
   * @return A resulting JSON object, never <code>null</code>
   */
  @Nonnull
  public static IJSONObject getMultiLingualTextAsJSON (@Nullable final IMultiLingualText aText)// NOPMD
  {
    final IJSONObject aJSON = new JSONObject ();
    if (aText != null)
    {
      for (final Map.Entry <Locale, String> aEntry : aText.getMap ().entrySet ())
      {
        aJSON.setStringProperty (aEntry.getKey ().toString (), aEntry.getValue ());
      }
    }
    return aJSON;
  }

  /**
   * Creates a multilingual text from the passed JSON objects property
   * specified.
   * 
   * @param aJSON
   *        the JSON object containing the multilingual text property
   * @param sAttributeName
   *        attribute that contains multi lingual text
   * @return the multi lingual values of the specified attribute in the pass
   *         JSON object
   */
  public static IMultiLingualText getMultiLingualTextProperty (@Nonnull final IJSONObject aJSONObject,
                                                               @Nonnull @Nonempty final String sProperty)
  {
    if (aJSONObject == null)
    {
      throw new NullPointerException ("aJSONObject"); //$NON-NLS-1$
    }
    if (StringHelper.hasNoText (sProperty))
    {
      throw new IllegalArgumentException ("sProperty must not be null or empty!"); //$NON-NLS-1$
    }
    final IJSONObject aMultilingualValues = aJSONObject.getObjectProperty (sProperty);
    if (aMultilingualValues == null)
    {
      throw new IllegalArgumentException ("No object property with the specified name " + sProperty + " found in the passed JSON Object"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return getMultiLingualText (aMultilingualValues);
  }

  public static IMultiLingualText getMultiLingualText (@Nonnull final IJSONObject aMultiLingualValues)
  {
    if (aMultiLingualValues == null)
    {
      throw new NullPointerException ("aMultiLingualValues"); //$NON-NLS-1$
    }
    final IMultiLingualText aMLT = new MultiLingualText ();
    for (final String sLanguage : aMultiLingualValues.getAllPropertyNames ())
    {
      final Locale aLocale = LocaleCache.getLocale (sLanguage);
      if (aLocale == null)
      {
        LOG.warn ("Unable to get locale for '" + sLanguage + "'!"); //$NON-NLS-1$ //$NON-NLS-2$
        continue;
      }
      final String sValue = aMultiLingualValues.getStringProperty (sLanguage);
      if (sValue == null)
      {
        LOG.warn ("Value in locale '" + sLanguage + "' is not a string!"); //$NON-NLS-1$ //$NON-NLS-2$
        continue;
      }
      aMLT.addText (aLocale, sValue);
    }
    return aMLT;
  }

  @Nonnull
  public static IJSONPropertyValueList <JSONPropertyValueString> getStringList (final List <String> aValues)
  {
    final IJSONPropertyValueList <JSONPropertyValueString> aList = new JSONPropertyValueList <JSONPropertyValueString> ();
    if (aValues != null)
    {
      for (final String sValue : aValues)
      {
        aList.addValue (new JSONPropertyValueString (sValue));
      }
    }
    return aList;
  }

  /**
   * Creates a property value list from the list of passed values
   * 
   * @param aValues
   *        An iterable container of values extending {@link IJSONPropertyValue}
   * @return The resulting {@link IJSONPropertyValueList}
   */
  @Nonnull
  public static IJSONPropertyValueList <IJSONPropertyValue <?>> getValueList (@Nullable final Iterable <? extends IJSONPropertyValue <?>> aValues)
  {
    return new JSONPropertyValueList <IJSONPropertyValue <?>> (aValues);
  }

  /**
   * Tries to parse the passed JSON into a String list and returns the list of
   * the contained Strings. If anything goes wrong, this method will return
   * null.
   * 
   * @param sJSON
   * @return The list of contained Strings, or <code>null</code>
   */
  @Nullable
  public static List <String> parseStringArray (final String sJSON)
  {
    return getArrayAsElementList (JSONReader.parseArraySafe (sJSON), String.class);
  }

  /**
   * Tries to parse the passed JSON into an object list and returns the list of
   * the contained JSON objects. If anything goes wrong, this method will return
   * null.
   * 
   * @param sJSON
   * @return list of the contained JSON objects
   */
  @Nullable
  public static List <IJSONObject> parseObjectArray (final String sJSON)
  {
    return getArrayAsElementList (JSONReader.parseArraySafe (sJSON), IJSONObject.class);
  }

  /**
   * Converts the passed {@link IJSON} array (only if it is actually of type
   * {@link IJSONPropertyValueList}) into a list of T and returns the list. If
   * anything goes wrong (e.g. one of the elements not of that type), this
   * method will return <code>null</code>.
   * 
   * @param aArray
   *        The JSON array to convert
   * @param aClass
   *        The expected class of the contained elements in the array
   * @return A list of elements, or <code>null</code>
   */
  @SuppressWarnings ("unchecked")
  @Nullable
  public static <T> List <T> getArrayAsElementList (@Nullable final IJSON aArray, final Class <T> aClass)
  {
    if (aArray == null)
    {
      return null;
    }

    if (aArray instanceof IJSONPropertyValueList)
    {
      final List <T> aResult = new ArrayList <T> ();
      for (final Object aToken : ((IJSONPropertyValueList <?>) aArray).getDataValues ())
      {
        if (aClass.isAssignableFrom (aToken.getClass ()))
        {
          aResult.add ((T) aToken);
        }
        else
        {
          LOG.error ("Unable to convert element of type " + aToken.getClass ().getName () + " to desired type " + aClass.getName () + " in passed JSON array: " + aArray.getJSONString ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          return null;
        }
      }
      return aResult;
    }
    return null;
  }

  /**
   * Tells whether the passed object is null or has no property at all
   * 
   * @param aObject
   * @return <code>true</code> if <code>null</code> or empty, <code>false</code>
   *         otherwise
   */
  public static boolean isNullOrEmpty (final IJSONObject aObject)
  {
    return aObject == null || aObject.isEmpty ();
  }

  /**
   * Creates a multi-list (list of lists) of JSONObjects from the passed
   * multi-array (rows of cells) of convertible elements. Any <code>null</code>
   * cell element will be substituted by an empty {@link IJSONObject} while
   * <code>null</code> rows will be substituted by empty list.
   * 
   * @param aMultiArray
   *        The multi-array of elements to convert, must not be
   *        <code>null</code>
   * @return The resulting multi-list of {@link IJSONObject}, never
   *         <code>null</code>
   */
  @Nonnull
  public static JSONPropertyValueList <IJSONPropertyValueList <IJSONObject>> getJSONMultiArray (@Nonnull final IJSONConvertible [][] aMultiArray)
  {
    if (aMultiArray == null)
    {
      throw new NullPointerException ("aMultiArray"); //$NON-NLS-1$
    }
    final JSONPropertyValueList <IJSONPropertyValueList <IJSONObject>> aList = new JSONPropertyValueList <IJSONPropertyValueList <IJSONObject>> ();
    for (final IJSONConvertible [] aRowArray : aMultiArray)
    {
      if (aRowArray == null)
      {
        aList.addValue (new JSONPropertyValueList <IJSONObject> ());// NOPMD
      }
      else
      {
        aList.addValue (new JSONPropertyValueList <IJSONObject> (getJSONObjectList (ContainerHelper.newList (aRowArray))));// NOPMD
      }
    }
    return aList;
  }

  @Nullable
  public static IJSONPropertyValue <?> getJSONValue (@Nullable final Object aValue)
  {
    if (aValue == null)
    {
      return null;
    }
    if (aValue instanceof IJSONPropertyValue <?>)
    {
      return (IJSONPropertyValue <?>) aValue;
    }
    if (aValue instanceof String)
    {
      return new JSONPropertyValueString ((String) aValue);
    }
    if (aValue instanceof Integer)
    {
      return new JSONPropertyValueInteger ((Integer) aValue);
    }
    if (aValue instanceof Boolean)
    {
      return new JSONPropertyValueBoolean ((Boolean) aValue);
    }
    if (aValue instanceof BigInteger)
    {
      return new JSONPropertyValueBigInteger ((BigInteger) aValue);
    }
    if (aValue instanceof BigDecimal)
    {
      return new JSONPropertyValueBigDecimal ((BigDecimal) aValue);
    }
    if (aValue instanceof Double)
    {
      return new JSONPropertyValueDouble ((Double) aValue);
    }
    if (aValue instanceof Long)
    {
      return new JSONPropertyValueLong ((Long) aValue);
    }
    LOG.warn ("Unable to create JSONPropertyValue from type " + //$NON-NLS-1$
              aValue.getClass ().getName () +
              "!"); //$NON-NLS-1$
    return null;
  }

  public static List <IJSONObject> getJSONObjectList (@Nullable final Iterable <? extends IJSONConvertible> aElements)
  {
    final List <IJSONObject> aList = ContainerHelper.newList ();
    if (aElements != null)
    {
      for (final IJSONConvertible aElement : aElements)
      {
        aList.add (aElement == null ? new JSONObject () : aElement.getAsJSON ());
      }
    }
    return aList;
  }
}
