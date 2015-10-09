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
package com.phloc.json2;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Base interface for a single JSON value
 * 
 * @author Philip Helger
 */
public interface IJsonValue extends IJson
{
  /**
   * @return The object value. May be <code>null</code>.
   */
  @Nullable
  Object getValue ();

  /**
   * Get the contained value casted to the specified class.
   * 
   * @param aClass
   *        The class to cast to.
   * @return The object value casted to the passed class. May be
   *         <code>null</code> if the contained value is <code>null</code>.
   * @throws ClassCastException
   *         in case of an error
   */
  @Nullable
  <T> T getCastedValue (@Nonnull Class <T> aClass);

  /**
   * Get the contained value converted using TypeConverter to the passed class.
   * 
   * @param aClass
   *        The class to convert to.
   * @return The object value casted to the passed class. May be
   *         <code>null</code> if the contained value is <code>null</code>.
   * @throws TypeConverterException
   *         in case of an error
   */
  @Nullable
  <T> T getConvertedValue (@Nonnull Class <T> aClass);

  /**
   * @return The class of the value or <code>null</code> if no value is
   *         contained.
   */
  @Nullable
  Class <?> getValueClass ();

  /**
   * @return <code>true</code> if the value is <code>null</code>. Same as
   *         <code>getValue()==null</code>.
   */
  boolean isNullValue ();

  boolean isBooleanValue ();

  boolean isIntValue ();

  boolean isDecimalValue ();

  boolean isStringValue ();

  /**
   * @return The serializer to be used. Never <code>null</code>.
   */
  @Nonnull
  IJsonValueSerializer getValueSerializer ();

  /**
   * Append this value in JSON notation to the passed {@link Writer}. This is a
   * shortcut for
   * <code>getValueSerializer ().appendAsJsonString (getValue (), aWriter);</code>
   * 
   * @param aWriter
   *        The destination. May not be <code>null</code>.
   */
  void appendAsJsonString (@Nonnull Writer aWriter) throws IOException;

  @Nonnull
  IJsonValue getClone ();
}
