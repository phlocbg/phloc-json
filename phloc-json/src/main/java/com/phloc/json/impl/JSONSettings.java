package com.phloc.json.impl;

public class JSONSettings
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final JSONSettings INSTANCE = new JSONSettings ();
  }

  public static boolean DEFAULT_PARSE_NULL_VALUES = false;
  private boolean m_bParseNullValues;

  /**
   * Ctor for singleton creation
   */
  protected JSONSettings ()
  {
    this.m_bParseNullValues = DEFAULT_PARSE_NULL_VALUES;
  }

  /**
   * Ctor
   * 
   * @return the singleton instance
   */
  public static JSONSettings getInstance ()
  {
    return SingletonHolder.INSTANCE;
  }

  public void setParseNullValues (final boolean bParseNull)
  {
    this.m_bParseNullValues = bParseNull;
  }

  public boolean isParseNullValues ()
  {
    return this.m_bParseNullValues;
  }
}
