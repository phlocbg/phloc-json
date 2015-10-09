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
package com.phloc.json;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;
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
  @Nonnull
  public static IJSONObject convertToJSON (@Nullable final Map <String, Object> aProperties)
  {
    return convertToJSON (aProperties, false);
  }

  /**
   * Converts the passed property map to a new JSON object containing all
   * properties from the map.<br/>
   * <b>ATTENTION:</b> Currently only simple types are supported, no container
   * types or nested objects!
   * 
   * @param aProperties
   *        The source map
   * @param bUseTypeConverter
   *        If <code>true</code> the type converter registry should be used to
   *        convert simple values.
   * @return the created JSON object
   */
  @Nonnull
  public static IJSONObject convertToJSON (@Nullable final Map <String, Object> aProperties,
                                           final boolean bUseTypeConverter)
  {
    final IJSONObject aJSON = new JSONObject ();
    if (aProperties != null)
    {
      // Sort map by key, to create reproducible results
      for (final Map.Entry <String, Object> aEntry : ContainerHelper.getSortedByKey (aProperties).entrySet ())
      {
        final String sPropertyName = aEntry.getKey ();
        final Object aValue = aEntry.getValue ();
        if (aValue == null)
        {
          // Special null keyword
          aJSON.setKeywordProperty (sPropertyName, CJSONConstants.KEYWORD_NULL);
        }
        else
          if (aValue instanceof IJSONConvertible)
          {
            // Object itself can be converted to JSON
            final IJSONObject aNestedValue = ((IJSONConvertible) aValue).getAsJSON ();
            aJSON.setObjectProperty (sPropertyName, aNestedValue);
          }
          else
            if (aValue instanceof Map <?, ?>)
            {
              // Its a nested map so recurse into it
              @SuppressWarnings ("unchecked")
              final IJSONObject aNestedValue = convertToJSON ((Map <String, Object>) aValue);
              aJSON.setObjectProperty (sPropertyName, aNestedValue);
            }
            else
            {
              // Main set property value
              aJSON.setProperty (sPropertyName, aValue, bUseTypeConverter);
            }
      }
    }
    return aJSON;
  }
}
