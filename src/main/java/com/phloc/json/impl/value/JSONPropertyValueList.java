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
package com.phloc.json.impl.value;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONPropertyValue;
import com.phloc.json.IJSONPropertyValueList;
import com.phloc.json.impl.CJSONConstants;
import com.phloc.json.impl.JSONParsingException;
import com.phloc.json.impl.JSONReader;

/**
 * Default implementation of {@link IJSONPropertyValueList}
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        The inner data type
 */
public class JSONPropertyValueList <DATATYPE> extends AbstractJSONPropertyValue <List <IJSONPropertyValue <DATATYPE>>> implements
                                                                                                                      IJSONPropertyValueList <DATATYPE>
{
  /**
   * Ctor
   */
  public JSONPropertyValueList (@Nullable final List <IJSONPropertyValue <DATATYPE>> aList)
  {
    super (ContainerHelper.newList (aList));
  }

  public JSONPropertyValueList ()
  {
    super (new ArrayList <IJSONPropertyValue <DATATYPE>> ());
  }

  public void addValue (@Nullable final IJSONPropertyValue <DATATYPE> aValue)
  {
    getData ().add (aValue);
  }

  public void addAllValues (@Nonnull final List <IJSONPropertyValue <DATATYPE>> aValues)
  {
    getData ().addAll (aValues);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <IJSONPropertyValue <DATATYPE>> getValues ()
  {
    return ContainerHelper.makeUnmodifiable (getData ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <DATATYPE> getDataValues ()
  {
    final List <DATATYPE> aDataValues = new ArrayList <DATATYPE> ();
    for (final IJSONPropertyValue <DATATYPE> aValue : getData ())
      aDataValues.add (aValue.getData ());
    return aDataValues;
  }

  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    appendNewLine (aResult, bAlignAndIndent);
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.LIST_START);
    appendNewLine (aResult, bAlignAndIndent);

    int nIndex = 0;
    for (final IJSONPropertyValue <DATATYPE> aValue : getData ())
    {
      if (!(aValue instanceof IJSONPropertyValueList) &&
          !(aValue instanceof IJSONObject) &&
          !(aValue instanceof JSONPropertyValueJSONObject))
      {
        indent (aResult, nLevel + 1, bAlignAndIndent);
      }
      if ((aValue instanceof IJSONObject) || (aValue instanceof JSONPropertyValueJSONObject))
      {
        // JSON object will increase the level already when passing through
        // PropertyValue method
        aValue.appendJSONString (aResult, bAlignAndIndent, nLevel);
      }
      else
      {
        aValue.appendJSONString (aResult, bAlignAndIndent, nLevel + 1);
      }

      if (nIndex < getData ().size () - 1)
        aResult.append (CJSONConstants.TOKEN_SEPARATOR);
      appendNewLine (aResult, bAlignAndIndent);
      nIndex++;
    }
    indent (aResult, nLevel, bAlignAndIndent);
    aResult.append (CJSONConstants.LIST_END);
  }

  /**
   * Converts the passed {@link ArrayNode} to a corresponding
   * {@link JSONPropertyValueList}
   * 
   * @param aValues
   * @return a property list representing the passed array
   * @throws JSONParsingException
   */
  @Nonnull
  public static JSONPropertyValueList <?> fromJSONNode (final ArrayNode aValues) throws JSONParsingException
  {
    // check the first token to determine list data type
    final JsonNode aFirst = ContainerHelper.getFirstElement (aValues);
    if (aFirst != null)
    {
      if (aFirst.isObject ())
        return JSONReader.getObjectList (aValues);
      if (aFirst.isArray ())
        return JSONReader.getSubList (aValues);
      if (aFirst.isBoolean ())
        return JSONReader.getBooleanList (aValues);
      if (aFirst.isInt ())
        return JSONReader.getIntegerList (aValues);
      if (aFirst.isTextual ())
        return JSONReader.getStringList (aValues);
      throw new JSONParsingException ("Unhandled value type: " + aFirst.getClass ());
    }
    // empty list, we cannot tell the type, so use Object
    return new JSONPropertyValueList <Object> ();
  }

  @Nonnull
  public JSONPropertyValueList <DATATYPE> getClone ()
  {
    return new JSONPropertyValueList <DATATYPE> (getData ());
  }
}
