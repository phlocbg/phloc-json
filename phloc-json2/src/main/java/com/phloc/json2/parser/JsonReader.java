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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.EUnicodeBOM;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.pair.ReadonlyPair;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.streamprovider.StringInputStreamProvider;
import com.phloc.commons.io.streams.NonBlockingStringReader;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.json2.IJson;
import com.phloc.json2.parser.errorhandler.DoNothingJsonParseExceptionHandler;
import com.phloc.json2.parser.errorhandler.IJsonParseExceptionHandler;
import com.phloc.json2.parser.errorhandler.LoggingJsonParseExceptionHandler;

/**
 * This is the central user class for reading and parsing Json from different
 * sources. This class reads full Json declarations only.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class JsonReader
{
  public static final Charset DEFAULT_CHARSET = CCharset.CHARSET_UTF_8_OBJ;

  private static final Logger s_aLogger = LoggerFactory.getLogger (JsonReader.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Use the LoggingJsonParseExceptionHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static IJsonParseExceptionHandler s_aDefaultParseExceptionHandler = new LoggingJsonParseExceptionHandler ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final JsonReader s_aInstance = new JsonReader ();

  private JsonReader ()
  {}

  /**
   * @return The default Json parse exception handler. May not be
   *         <code>null</code>. For backwards compatibility reasons this is be
   *         default an instance of {@link LoggingJsonParseExceptionHandler}.
   */
  @Nonnull
  public static IJsonParseExceptionHandler getDefaultParseExceptionHandler ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aDefaultParseExceptionHandler;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the default Json parse exception handler (for unrecoverable errors).
   *
   * @param aDefaultParseExceptionHandler
   *        The new default exception handler to be used. May not be
   *        <code>null</code>.
   */
  public static void setDefaultParseExceptionHandler (@Nonnull final IJsonParseExceptionHandler aDefaultParseExceptionHandler)
  {
    ValueEnforcer.notNull (aDefaultParseExceptionHandler, "DefaultParseExceptionHandler");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aDefaultParseExceptionHandler = aDefaultParseExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Main reading of the Json
   *
   * @param aStream
   *        The stream to read from. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        A custom handler for unrecoverable errors. May not be
   *        <code>null</code>.
   * @return <code>null</code> if parsing failed with an unrecoverable error
   *         (and no throwing exception handler is used), or <code>null</code>
   *         if a recoverable error occurred or non-<code>null</code> if parsing
   *         succeeded.
   */
  @Nullable
  private static JsonNode _readJson (@Nonnull final CharStream aStream,
                                     @Nonnull final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    final ParserJsonTokenManager aTokenHdl = new ParserJsonTokenManager (aStream);
    final ParserJson aParser = new ParserJson (aTokenHdl);
    try
    {
      // Main parsing
      return aParser.json ();
    }
    catch (final ParseException ex)
    {
      // Unrecoverable error
      aCustomExceptionHandler.onException (ex);
      return null;
    }
  }

  /**
   * Check if the passed Json file can be parsed without error using the default
   * charset.
   *
   * @param aFile
   *        The file to be parsed. May not be <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull final File aFile)
  {
    return isValidJson (aFile, DEFAULT_CHARSET);
  }

  /**
   * Check if the passed Json file can be parsed without error
   *
   * @param aFile
   *        The file to be parsed. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used for reading the Json file in case no BOM is
   *        present. May not be <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull final File aFile, @Nonnull final Charset aFallbackCharset)
  {
    return isValidJson (new FileSystemResource (aFile), aFallbackCharset);
  }

  /**
   * Check if the passed Json resource can be parsed without error using the
   * default charset
   *
   * @param aISP
   *        The resource to be parsed. May not be <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull final IInputStreamProvider aISP)
  {
    return isValidJson (aISP, DEFAULT_CHARSET);
  }

  /**
   * Check if the passed Json resource can be parsed without error
   *
   * @param aISP
   *        The resource to be parsed. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used for reading the Json file in case no BOM is
   *        present. May not be <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull final IInputStreamProvider aISP, @Nonnull final Charset aFallbackCharset)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");

    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
    {
      s_aLogger.warn ("Failed to open Json InputStream from " + aISP);
      return false;
    }
    return isValidJson (aIS, aFallbackCharset);
  }

  /**
   * Check if the passed input stream can be resembled to valid Json content.
   * This is accomplished by fully parsing the Json file each time the method is
   * called using the default charset
   *
   * @param aIS
   *        The input stream to use. Is automatically closed. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the Json is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull @WillClose final InputStream aIS)
  {
    return isValidJson (aIS, DEFAULT_CHARSET);
  }

  /**
   * Check if the passed input stream can be resembled to valid Json content.
   * This is accomplished by fully parsing the Json file each time the method is
   * called. This is similar to calling
   * {@link #readFromStream(IInputStreamProvider,Charset)} and checking for a
   * non-<code>null</code> result.
   *
   * @param aIS
   *        The input stream to use. Is automatically closed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no BOM is present. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the Json is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull @WillClose final InputStream aIS, @Nonnull final Charset aFallbackCharset)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");

    return isValidJson (StreamUtils.createReader (aIS, aFallbackCharset));
  }

  /**
   * Check if the passed String can be resembled to valid Json content. This is
   * accomplished by fully parsing the Json file each time the method is called.
   * This is similar to calling {@link #readFromString(String, Charset)} and
   * checking for a non-<code>null</code> result.
   *
   * @param sJson
   *        The Json string to scan. May not be <code>null</code>.
   * @return <code>true</code> if the Json is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull final String sJson)
  {
    ValueEnforcer.notNull (sJson, "Json");

    return isValidJson (new NonBlockingStringReader (sJson));
  }

  /**
   * Check if the passed reader can be resembled to valid Json content. This is
   * accomplished by fully parsing the Json each time the method is called. This
   * is similar to calling
   * {@link #readFromStream(IInputStreamProvider, Charset)} and checking for a
   * non-<code>null</code> result.
   *
   * @param aReader
   *        The reader to use. May not be <code>null</code>.
   * @return <code>true</code> if the Json is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidJson (@Nonnull @WillClose final Reader aReader)
  {
    ValueEnforcer.notNull (aReader, "Reader");

    try
    {
      final JsonCharStream aCharStream = new JsonCharStream (aReader);
      final JsonNode aNode = _readJson (aCharStream, DoNothingJsonParseExceptionHandler.getInstance ());
      return aNode != null;
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }

  /**
   * Read the Json from the passed String using a byte stream.
   *
   * @param sJson
   *        The source string containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no BOM is present. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromString (@Nonnull final String sJson, @Nonnull final Charset aFallbackCharset)
  {
    return readFromString (sJson, aFallbackCharset, null);
  }

  /**
   * Read the Json from the passed String using a byte stream.
   *
   * @param sJson
   *        The source string containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no BOM is present. May not be
   *        <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromString (@Nonnull final String sJson,
                                      @Nonnull final Charset aFallbackCharset,
                                      @Nullable final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new StringInputStreamProvider (sJson, aFallbackCharset),
                           aFallbackCharset,
                           aCustomExceptionHandler);
  }

  /**
   * Read the Json from the passed String using a character stream. An
   * eventually contained <code>@charset</code> rule is ignored.
   *
   * @param sJson
   *        The source string containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromString (@Nonnull final String sJson)
  {
    return readFromReader (new NonBlockingStringReader (sJson), (IJsonParseExceptionHandler) null);
  }

  /**
   * Read the Json from the passed String using a character stream.
   *
   * @param sJson
   *        The source string containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromString (@Nonnull final String sJson,
                                      @Nullable final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromReader (new NonBlockingStringReader (sJson), aCustomExceptionHandler);
  }

  /**
   * Read the Json from the passed File using the default charset.
   *
   * @param aFile
   *        The file containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromFile (@Nonnull final File aFile)
  {
    return readFromFile (aFile, DEFAULT_CHARSET);
  }

  /**
   * Read the Json from the passed File.
   *
   * @param aFile
   *        The file containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no is BOM is present. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromFile (@Nonnull final File aFile, @Nonnull final Charset aFallbackCharset)
  {
    return readFromFile (aFile, aFallbackCharset, null);
  }

  /**
   * Read the Json from the passed File.
   *
   * @param aFile
   *        The file containing the Json to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no BOM is present. May not be
   *        <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromFile (@Nonnull final File aFile,
                                    @Nonnull final Charset aFallbackCharset,
                                    @Nullable final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new FileSystemResource (aFile), aFallbackCharset, aCustomExceptionHandler);
  }

  /**
   * Read the Json from the passed {@link IInputStreamProvider} using the
   * default charset.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromStream (@Nonnull final IInputStreamProvider aISP)
  {
    return readFromStream (aISP, DEFAULT_CHARSET);
  }

  /**
   * Read the Json from the passed {@link IInputStreamProvider}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used if no BOM is present. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromStream (@Nonnull final IInputStreamProvider aISP, @Nonnull final Charset aFallbackCharset)
  {
    return readFromStream (aISP, aFallbackCharset, null);
  }

  /**
   * Read the Json from the passed {@link IInputStreamProvider}.
   *
   * @param aISP
   *        The input stream to use. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no BOM is present. May not be
   *        <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromStream (@Nonnull final IInputStreamProvider aISP,
                                      @Nonnull final Charset aFallbackCharset,
                                      @Nullable final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");
    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;
    return readFromStream (aIS, aFallbackCharset, aCustomExceptionHandler);
  }

  /**
   * Read the Json from the passed {@link InputStream} using the default
   * charset.
   *
   * @param aIS
   *        The input stream to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromStream (@Nonnull final InputStream aIS)
  {
    return readFromStream (aIS, DEFAULT_CHARSET);
  }

  /**
   * Read the Json from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used if no BOM is present. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromStream (@Nonnull final InputStream aIS, @Nonnull final Charset aFallbackCharset)
  {
    return readFromStream (aIS, aFallbackCharset, null);
  }

  /**
   * If a BOM is present in the {@link InputStream} it is read and if possible
   * the charset is automatically determined from the BOM.
   *
   * @param aIS
   *        The input stream to use. May not be <code>null</code>.
   * @return <code>null</code> if no InputStream could be opened, the pair with
   *         non-<code>null</code> {@link InputStream} and a potentially
   *         <code>null</code> {@link Charset} otherwise.
   */
  @Nullable
  private static ReadonlyPair <InputStream, Charset> _getInputStreamWithoutBOM (@Nonnull final InputStream aIS)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    // Check for BOM
    final int nMaxBOMBytes = EUnicodeBOM.getMaximumByteCount ();
    final PushbackInputStream aPIS = new PushbackInputStream (aIS, nMaxBOMBytes);
    try
    {
      final byte [] aBOM = new byte [nMaxBOMBytes];
      final int nReadBOMBytes = aPIS.read (aBOM);
      Charset aDeterminedCharset = null;
      if (nReadBOMBytes > 0)
      {
        // Some byte BOMs were read
        final EUnicodeBOM eBOM = EUnicodeBOM.getFromBytesOrNull (ArrayHelper.getCopy (aBOM, 0, nReadBOMBytes));
        if (eBOM == null)
        {
          // Unread the whole BOM
          aPIS.unread (aBOM, 0, nReadBOMBytes);
        }
        else
        {
          // Unread the unnecessary parts of the BOM
          final int nBOMBytes = eBOM.getByteCount ();
          if (nBOMBytes < nReadBOMBytes)
            aPIS.unread (aBOM, nBOMBytes, nReadBOMBytes - nBOMBytes);

          // Use the Charset of the BOM - maybe null!
          aDeterminedCharset = eBOM.getCharset ();
        }
      }
      return new ReadonlyPair <InputStream, Charset> (aPIS, aDeterminedCharset);
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Failed to determine BOM", ex);
      return null;
    }
  }

  /**
   * Read the Json from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case no BOM is present. May not be
   *        <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromStream (@Nonnull final InputStream aIS,
                                      @Nonnull final Charset aFallbackCharset,
                                      @Nullable final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");

    // Open input stream
    final ReadonlyPair <InputStream, Charset> aISAndBOM = _getInputStreamWithoutBOM (aIS);
    if (aISAndBOM == null || aISAndBOM.getFirst () == null)
    {
      // Failed to open stream!
      return null;
    }

    final InputStream aISToUse = aISAndBOM.getFirst ();
    final Charset aCharsetToUse = aISAndBOM.getSecond () != null ? aISAndBOM.getSecond () : aFallbackCharset;

    try
    {
      final JsonCharStream aCharStream = new JsonCharStream (StreamUtils.createReader (aISToUse, aCharsetToUse));

      // Use the default Json exception handler if none is provided
      final IJsonParseExceptionHandler aRealExceptionHandler = aCustomExceptionHandler == null ? getDefaultParseExceptionHandler ()
                                                                                              : aCustomExceptionHandler;
      final JsonNode aNode = _readJson (aCharStream, aRealExceptionHandler);

      // Failed to interpret content as Json?
      if (aNode == null)
        return null;

      // Convert the AST to a domain object
      return JsonHandler.readCascadingStyleSheetFromNode (aNode);
    }
    finally
    {
      StreamUtils.close (aISToUse);
      StreamUtils.close (aIS);
    }
  }

  /**
   * Read the Json from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromReader (@Nonnull final Reader aReader)
  {
    return readFromReader (aReader, null);
  }

  /**
   * Read the Json from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the Json declarations
   *         otherwise.
   */
  @Nullable
  public static IJson readFromReader (@Nonnull final Reader aReader,
                                      @Nullable final IJsonParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aReader, "Reader");

    // No charset determination, as the Reader already has an implicit Charset

    try
    {
      final JsonCharStream aCharStream = new JsonCharStream (aReader);

      // Use the default Json exception handler if none is provided
      final IJsonParseExceptionHandler aRealExceptionHandler = aCustomExceptionHandler == null ? getDefaultParseExceptionHandler ()
                                                                                              : aCustomExceptionHandler;

      final JsonNode aNode = _readJson (aCharStream, aRealExceptionHandler);

      // Failed to interpret content as Json?
      if (aNode == null)
        return null;

      // Convert the AST to a domain object
      return JsonHandler.readCascadingStyleSheetFromNode (aNode);
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }

  // public static void parseAsArray (@Nonnull final String sJSON, @Nonnull
  // final JsonArray aArray) throws JsonReadException
  // {
  // final JsonNode aNode = readFromString (sJSON);
  // if (!aNode.isArray ())
  // throw new JsonReadException ("Passed string is not a JSON array!");
  // _convertArray ((ArrayNode) aNode, aArray);
  // }
  //
  // public static void parseAsObject (@Nonnull final String sJSON, @Nonnull
  // final JsonObject aObject) throws JsonReadException
  // {
  // final JsonNode aNode = JacksonHelper.parseToNode (sJSON);
  // if (!aNode.isObject ())
  // throw new JsonReadException ("Passed string is not a JSON object!");
  // _convertObject ((ObjectNode) aNode, aObject);
  // }
  //
  // @Nonnull
  // public static JsonValue parseAsValue (@Nonnull final String sJSON) throws
  // JsonReadException
  // {
  // final JsonNode aNode = JacksonHelper.parseToNode (sJSON);
  // if (aNode.isContainerNode ())
  // throw new JsonReadException ("Passed string is not a JSON value!");
  // return convertValue (aNode);
  // }
}
