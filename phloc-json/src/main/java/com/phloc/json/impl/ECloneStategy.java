package com.phloc.json.impl;

import javax.annotation.Nullable;

public enum ECloneStategy
{
 FORCE ("force"), //$NON-NLS-1$
 AVOID ("avoid"), //$NON-NLS-1$
 INHERIT ("inherit"); //$NON-NLS-1$

  private String m_sID;

  private ECloneStategy (final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  /**
   * Tries to resolve the enum entry corresponding to the passed ID
   * 
   * @param sID
   * @return The resolved enum entry, or <code>null</code>
   */
  @Nullable
  public static ECloneStategy getFromID (final String sID)
  {
    for (final ECloneStategy eValue : values ())
    {
      if (eValue.getID ().equals (sID))
      {
        return eValue;
      }
    }
    return null;
  }

}
