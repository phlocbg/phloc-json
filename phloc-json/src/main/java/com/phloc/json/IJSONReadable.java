package com.phloc.json;

import javax.annotation.Nonnull;

/**
 * Interface for objects that can read their state from a JSON object. Generally
 * you will have a public Ctor not doing anything except calling this
 * 
 * @author Boris Gregorcic
 */
public interface IJSONReadable
{
  /**
   * Reads all data from the passed JSON objects and restores the state of this
   * object
   * 
   * @param aJSON
   *        the JSON from which to restore the state
   */
  void readFromJSON (@Nonnull IJSONObject aJSON);
}
