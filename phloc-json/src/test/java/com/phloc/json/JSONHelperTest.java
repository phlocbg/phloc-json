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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link JSONHelper}.
 * 
 * @author Philip Helger
 */
@Deprecated
public final class JSONHelperTest
{
  private static final String STRING_UNESCAPED = "this is a \"test\" containing \b \t \n \f \r \\ / <>\n##+{}()"; //$NON-NLS-1$
  private static final String STRING_ESCAPED = "this is a \\\"test\\\" containing \\b \\t \\n \\f \\r \\\\ / <>\\n##+{}()"; //$NON-NLS-1$

  @SuppressWarnings ("static-method")
  @Test
  public void testEscape ()
  {
    assertEquals (STRING_ESCAPED, JSONHelper.jsonEscape (STRING_UNESCAPED));
  }
}
