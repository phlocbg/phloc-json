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
  public static final boolean DEFAULT_SIDEEFFECT_DETECTION = false;
  public static final boolean DEFAULT_CLONE_PROPERTIES = true;
  public static final boolean DEFAULT_CYCLE_DETECTION = false;

  private boolean m_bParseNullValues;
  private boolean m_bCloneProperties = DEFAULT_CLONE_PROPERTIES;
  private boolean m_bCycleDetection = DEFAULT_CYCLE_DETECTION;
  private boolean m_bSideEffectDetection = DEFAULT_SIDEEFFECT_DETECTION;

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
   * Enables or disables cycle detection (to avoid object reference cycles)
   *
   * @param bDetectCycles
   *        Whether cycle detection is active (default:
   *        {@value #DEFAULT_CYCLE_DETECTION})
   */
  public void setCycleDetection (final boolean bDetectCycles)
  {
    this.m_bCycleDetection = bDetectCycles;
  }

  /**
   * @return Whether or not cycle detection is on
   */
  public boolean isDetectCycles ()
  {
    return this.m_bCycleDetection;
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

  /**
   * Enables or disables the side effect detection (see
   * {{@link #isDetectSideEffects()}). This will only have effect if also cycle
   * detection is active ()see {{@link #isDetectCycles()})
   *
   * @param bDetectSideEffects
   *        Whether side effect detection is active (default:
   *        {@value #DEFAULT_SIDEEFFECT_DETECTION})
   */
  public void setDetectSideEffects (final boolean bDetectSideEffects)
  {
    this.m_bSideEffectDetection = bDetectSideEffects;
  }

  /**
   * @return Whether or not side effect detection is on. This will warn on
   *         reused objects (having multiple parents) and will emit errors when
   *         actually changing properties inside such a shared object
   */
  public boolean isDetectSideEffects ()
  {
    return this.m_bSideEffectDetection;
  }
}
