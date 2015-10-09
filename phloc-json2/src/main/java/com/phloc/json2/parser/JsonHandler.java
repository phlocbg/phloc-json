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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.json2.IJson;

/**
 * This class is the entry point for converting AST nodes from the parser to
 * domain objects. This class is only used internally.
 * 
 * @author Philip Helger
 */
@Immutable
public final class JsonHandler
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final JsonHandler s_aInstance = new JsonHandler ();

  private JsonHandler ()
  {}

  /**
   * Create a {@link IJson} object from a parsed object.
   * 
   * @param aNode
   *        The parsed Json object to read. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IJson readCascadingStyleSheetFromNode (@Nonnull final JsonNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aNode.getNodeType () != ParserJsonTreeConstants.JJTROOT)
      throw new JsonHandlingException (aNode, "Passed node is not a root node!");

    return new JsonNodeToDomainObject ().createCascadingStyleSheetFromNode (aNode);
  }
}
