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
package com.phloc.json2.parser;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringParser;
import com.phloc.json2.IJson;
import com.phloc.json2.convert.JsonConverter;
import com.phloc.json2.impl.JsonValue;

/**
 * This class converts the jjtree node to a domain object. This is where the
 * hard work happens.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
final class JsonNodeToDomainObject
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JsonNodeToDomainObject.class);

  /**
   * Constructor
   */
  public JsonNodeToDomainObject ()
  {}

  private void _expectNodeType (@Nonnull final JsonNode aNode, @Nonnull final int nExpected)
  {
    if (aNode.getNodeType () != nExpected)
      throw new JsonHandlingException (aNode, "Expected a '" +
                                              ParserJsonTreeConstants.jjtNodeName[nExpected] +
                                              "' node but received a '" +
                                              ParserJsonTreeConstants.jjtNodeName[aNode.getNodeType ()] +
                                              "'");
  }

  private static void _throwUnexpectedChildrenCount (@Nonnull final JsonNode aNode, @Nonnull @Nonempty final String sMsg)
  {
    s_aLogger.error (sMsg);
    for (int i = 0; i < aNode.jjtGetNumChildren (); ++i)
      s_aLogger.error ("  " + aNode.jjtGetChild (i));
    throw new JsonHandlingException (aNode, sMsg);
  }

  public IJson _createValue (@Nonnull final JsonNode aNode)
  {
    _expectNodeType (aNode, ParserJsonTreeConstants.JJTVALUE);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount != 1)
      _throwUnexpectedChildrenCount (aNode, "Expected exactly 1 child but got " + nChildCount + " children!");

    final JsonNode aChildNode = aNode.jjtGetChild (0);
    switch (aChildNode.getNodeType ())
    {
      case ParserJsonTreeConstants.JJTJSTRING:
        return JsonConverter.convertToJson (aChildNode.getText ());
      case ParserJsonTreeConstants.JJTJNUMBER:
        return JsonValue.create (StringParser.parseBigDecimal (aChildNode.getText ()));
      case ParserJsonTreeConstants.JJTJOBJECT:
        break;
      case ParserJsonTreeConstants.JJTJARRAY:
        break;
      case ParserJsonTreeConstants.JJTJTRUE:
        return JsonValue.TRUE;
      case ParserJsonTreeConstants.JJTJFALSE:
        return JsonValue.FALSE;
      case ParserJsonTreeConstants.JJTJNULL:
        return JsonValue.NULL;
      default:
        throw new JsonHandlingException (aNode, "Unexpected value child: " + aChildNode.getNodeType ());
    }

    return null;
  }

  @Nonnull
  public IJson createCascadingStyleSheetFromNode (@Nonnull final JsonNode aNode)
  {
    _expectNodeType (aNode, ParserJsonTreeConstants.JJTROOT);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount != 1)
      _throwUnexpectedChildrenCount (aNode, "Expected exactly 1 child but got " + nChildCount + " children!");

    final JsonNode aChildNode = aNode.jjtGetChild (0);
    return _createValue (aChildNode);
  }
}
