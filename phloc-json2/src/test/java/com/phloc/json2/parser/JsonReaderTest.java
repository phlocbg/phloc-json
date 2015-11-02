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

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.json2.IJson;
import com.phloc.json2.IJsonObject;
import com.phloc.json2.serialize.JsonHelper;

public class JsonReaderTest
{
  private static final String MOCK_JSON_MINI = "com/phloc/json/mock/mockLibraryMini.json"; //$NON-NLS-1$
  private static final String MOCK_JSON_MEDIUM = "com/phloc/json/mock/mockLibrary.json"; //$NON-NLS-1$
  private static final String MOCK_JSON_FULL = "com/phloc/json/mock/mockLibraryFull.json"; //$NON-NLS-1$

  @Test
  public void testBasic ()
  {
    for (final String sJson : new String [] { "true",
                                             "false",
                                             "null",
                                             "10",
                                             "0",
                                             "199999",
                                             "-10",
                                             "-0",
                                             "-199999",
                                             "1.5",
                                             "1.0",
                                             "1.00000",
                                             "-1.5",
                                             "-1.0",
                                             "-1.00000",
                                             "10e+3",
                                             "0e+3",
                                             "199999e+3",
                                             "-10e+3",
                                             "-0e+3",
                                             "-199999e+3",
                                             "1.5e+3",
                                             "1.0e+3",
                                             "1.00000e+3",
                                             "-1.5e+3",
                                             "-1.0e+3",
                                             "-1.00000e+3",
                                             "10E15",
                                             "0E15",
                                             "199999E15",
                                             "-10E15",
                                             "-0E15",
                                             "-199999E15",
                                             "1.5E15",
                                             "1.0E15",
                                             "1.00000E15",
                                             "-1.5E15",
                                             "-1.0E15",
                                             "-1.00000E15",
                                             "\"abc'def'hgi\"",
                                             "\"ab\\\"ab\\/ab\\\\aa\\bab\\nab\\fab\\rab\\nab\\tab\"",
                                             "\"ab\\u1234ab\"",
                                             "[]",
                                             "[3]",
                                             "[3,4,5]",
                                             "[3,\"abc\",4,[],5]",
                                             "{}",
                                             "{\"key\":56}",
                                             "{\"key\":56, \"value2\": \"abc\"}",
                                             "  {  \"key\"  :  -1.00000E15  ,  \"value2\"  :  \"abc\"  }  " })
    {
      final IJson aJson = JsonReader.readFromString (sJson);
      assertNotNull ("Failed to parse: " + sJson, aJson);
    }
  }

  @Test
  public void testParseMini ()
  {
    final String sJSON = StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_MINI),
                                                          CCharset.CHARSET_UTF_8_OBJ);
    final IJson aJSON = JsonReader.readFromString (sJSON);
    assertNotNull ("Failed to parse: " + sJSON, aJSON);
  }

  @Test
  public void testParseMedium ()
  {
    final String sJSON = StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_MEDIUM),
                                                          CCharset.CHARSET_UTF_8_OBJ);
    final IJson aJSON = JsonReader.readFromString (sJSON);
    assertNotNull ("Failed to parse: " + sJSON, aJSON);
  }

  @Test
  public void testParseFull ()
  {
    final String sJSON = StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (MOCK_JSON_FULL),
                                                          CCharset.CHARSET_UTF_8_OBJ);
    final IJson aJSON = JsonReader.readFromString (sJSON);
    assertNotNull ("Failed to parse: " + sJSON, aJSON);
  }

  @Test
  public void testParseSlash ()
  {
    final String sJSON = "{\"prop\":\"aaa/bbb\"}";
    final IJson aJSON = JsonReader.readFromString (sJSON);
    assertNotNull ("Failed to parse: " + sJSON, aJSON);
  }

  @Test
  public void testParseSlashEscaped ()
  {
    final String sJSON = "{\"prop\":\"aaa\\/bbb\"}";
    final IJson aJSON = JsonReader.readFromString (sJSON);
    assertNotNull ("Failed to parse: " + sJSON, aJSON);
  }

  @Test
  public void testEscaping ()
  {
    assertEscaped ("aaa\"bbb");
    assertEscaped ("aaa\\\"bbb");
    assertEscaped ("aaa\\/bbb");
    assertEscaped ("aaa\bbbb");
  }

  private static void assertEscaped (final String sValue)
  {
    final String sJSON = "{\"prop\":\"" + JsonHelper.jsonEscape (sValue) + "\"}";
    final IJson aJSON = JsonReader.readFromString (sJSON);
    Assert.assertEquals (sValue, ((IJsonObject) aJSON).getValue ("prop").getValue ());

  }

}
