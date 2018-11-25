package com.phloc.json.impl;

import java.util.concurrent.atomic.AtomicLong;

public class JSONStatistics
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final JSONStatistics INSTANCE = new JSONStatistics ();
  }

  public static final boolean DEFAULT_TRACK_OBJECTS = false;
  public static final boolean DEFAULT_TRACK_PROPERTIES = false;
  public static final boolean DEFAULT_TRACK_PROPERTY_VALUES = false;

  private final AtomicLong m_aObjectCounter = new AtomicLong (0);
  private final AtomicLong m_aPropertyCounter = new AtomicLong (0);
  private final AtomicLong m_aPropertyValueCounter = new AtomicLong (0);

  private boolean m_bTrackObjects;
  private boolean m_bTrackProperties;
  private boolean m_bTrackPropertyValues;

  /**
   * Ctor for singleton creation
   */
  protected JSONStatistics ()
  {
    this.m_bTrackObjects = DEFAULT_TRACK_OBJECTS;
    this.m_bTrackProperties = DEFAULT_TRACK_PROPERTIES;
    this.m_bTrackPropertyValues = DEFAULT_TRACK_PROPERTY_VALUES;
  }

  /**
   * Ctor
   * 
   * @return the singleton instance
   */
  public static JSONStatistics getInstance ()
  {
    return SingletonHolder.INSTANCE;
  }

  public long getObjectCount ()
  {
    return this.m_aObjectCounter.get ();
  }

  public long getPropertyCount ()
  {
    return this.m_aPropertyCounter.get ();
  }

  public long getPropertyValueCount ()
  {
    return this.m_aPropertyValueCounter.get ();
  }

  public void start ()
  {
    setTracking (true, true, true);
  }

  public void setTracking (final boolean bTrackObjects,
                           final boolean bTrackProperties,
                           final boolean bTrackPropertyValues)
  {
    this.m_bTrackObjects = bTrackObjects;
    this.m_bTrackProperties = bTrackProperties;
    this.m_bTrackPropertyValues = bTrackPropertyValues;
  }

  public void onObjectCreated ()
  {
    if (this.m_bTrackObjects)
    {
      this.m_aObjectCounter.incrementAndGet ();
    }
  }

  public void onPropertyCreated ()
  {
    if (this.m_bTrackProperties)
    {
      this.m_aPropertyCounter.incrementAndGet ();
    }
  }

  public void onPropertyValueCreated ()
  {
    if (this.m_bTrackPropertyValues)
    {
      this.m_aPropertyValueCounter.incrementAndGet ();
    }
  }

  public void stop ()
  {
    setTracking (true, true, true);
  }

  public void reset ()
  {
    this.m_aObjectCounter.set (0);
    this.m_aPropertyCounter.set (0);
    this.m_aPropertyValueCounter.set (0);
  }
}
