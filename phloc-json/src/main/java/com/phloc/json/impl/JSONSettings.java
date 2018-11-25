package com.phloc.json.impl;

/**
 * JSONSettings let you steer certain behavior globally
 * 
 * @author Boris Gregorcic
 */
public class JSONSettings
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final JSONSettings INSTANCE = new JSONSettings ();
  }

  public static final boolean DEFAULT_PARSE_NULL_VALUES = false;
  public static final boolean DEFAULT_CLONE_PROPERTIES = true;

  private boolean m_bParseNullValues;
  private boolean m_bCloneProperties = DEFAULT_CLONE_PROPERTIES;

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

  /**
   * Defines whether or not cloning is used when assigning properties and values
   * in order to avoid side effects between read and set values
   * 
   * @param bCloneProperties
   *        <code>true</code> if cloning is used or <code>false</code>
   *        otherwise. The default is <code>true</code>
   */
  public void setCloneProperties (final boolean bCloneProperties)
  {
    this.m_bCloneProperties = bCloneProperties;
  }

  /**
   * This tells you whether or not cloning is used when assigning properties and
   * values in order to avoid side effects between read and set values
   * 
   * @return <code>true</code> if cloning is used or <code>false</code>
   *         otherwise. The default is <code>true</code> but this can be changed
   *         at runtime
   */
  public boolean isCloneProperties ()
  {
    return this.m_bCloneProperties;
  }
}
