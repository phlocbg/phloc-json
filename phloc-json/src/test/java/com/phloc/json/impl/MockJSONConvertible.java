package com.phloc.json.impl;

import com.phloc.json.IJSONConvertible;
import com.phloc.json.IJSONObject;

public class MockJSONConvertible implements IJSONConvertible
{
  private final String m_sValue;

  public MockJSONConvertible (final String sValue)
  {
    this.m_sValue = sValue;
  }

  @Override
  public IJSONObject getAsJSON ()
  {
    final JSONObject aJSON = new JSONObject ();
    aJSON.setStringProperty ("value", this.m_sValue); //$NON-NLS-1$
    return aJSON;
  }
}
