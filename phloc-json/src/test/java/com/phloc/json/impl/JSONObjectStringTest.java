package com.phloc.json.impl;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.json.IJSONObject;

public class JSONObjectStringTest
{
  @Test
  public void testBig () throws JSONParsingException, JSONCompressException
  {
    final IJSONObject aNative = JSONReader.parseObject (StreamUtils.getAllBytesAsString (ClassPathResource.getInputStream (JSONReaderTest.MOCK_JSON_LIB_FULL),
                                                                                         CCharset.CHARSET_UTF_8_OBJ));

    final JSONObjectString aString = new JSONObjectString (aNative);

    Assert.assertEquals (aNative.getJSONString (false), aString.getJSONString ());
    Assert.assertEquals (aNative, aString.getNative ());
  }
}
