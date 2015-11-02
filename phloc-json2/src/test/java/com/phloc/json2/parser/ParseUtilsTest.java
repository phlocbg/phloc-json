/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.json2.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link ParseUtils}.
 * 
 * @author Boris Gregorcic
 */
public final class ParseUtilsTest
{
  @Test
  public void testUnescapeString ()
  {
    assertEquals ("aaa/aaa", ParseUtils.unescapeString (new StringBuilder ("\"aaa/aaa\"")));
    assertEquals ("aaa\"aaa", ParseUtils.unescapeString (new StringBuilder ("\"aaa\\\"aaa\"")));
    assertEquals ("aaa/aaa", ParseUtils.unescapeString (new StringBuilder ("\"aaa\\/aaa\"")));
    assertEquals ("aaa\baaa\"aaa\"", ParseUtils.unescapeString (new StringBuilder ("\"aaa\\baaa\"aaa\\\"\"")));
    assertEquals ("/\\/\baaa", ParseUtils.unescapeString (new StringBuilder ("\"\\/\\\\/\\baaa\"")));
  }
}
