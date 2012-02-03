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

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

import com.phloc.json.impl.CJSONConstants;
import com.phloc.json.impl.JSONObject;

/**
 * General utility class for JSON string operations like escaping and unescaping
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class JSONComplexUtils
{
  private JSONComplexUtils ()
  {}

  /**
   * Converts the passed property map to a new JSON object containing all
   * properties from the map.<br/>
   * <b>ATTENTION:</b> Currently only simple types are supported, no container
   * types or nested objects!
   * 
   * @param aProperties
   * @return the created JSON object
   */
  // TODO BG: extend to be able to process nested containers and objects (e.g
  // IJSONConvertible)
  public static IJSONObject convertToJSON (@CheckForNull final Map <String, Object> aProperties)
  {
    final IJSONObject aJSON = new JSONObject ();
    if (aProperties != null)
      for (final Map.Entry <String, Object> aEntry : aProperties.entrySet ())
      {
        final String sPropertyName = aEntry.getKey ();
        final Object aValue = aEntry.getValue ();
        if (aValue == null)
          aJSON.setKeywordProperty (sPropertyName, CJSONConstants.KEYWORD_NULL);
        else
          if (aValue instanceof String)
            aJSON.setStringProperty (sPropertyName, (String) aValue);
          else
            if (aValue instanceof Integer)
              aJSON.setIntegerProperty (sPropertyName, ((Integer) aValue).intValue ());
            else
              if (aValue instanceof Boolean)
                aJSON.setBooleanProperty (sPropertyName, ((Boolean) aValue).booleanValue ());
              else
                if (aValue instanceof Double)
                  aJSON.setDoubleProperty (sPropertyName, ((Double) aValue).doubleValue ());
                else
                  throw new UnsupportedOperationException ("JSON conversion is currently not possible for values of type " +
                                                           aValue.getClass ().getName ());
      }
    return aJSON;
  }
}
