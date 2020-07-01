package com.phloc.json.impl;

import javax.annotation.Nonnull;

import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.IJSONString;

public class JSONObjectString extends AbstractJSONString <IJSONObject> implements IJSONString <IJSONObject>
{
  private static final long serialVersionUID = 9016882425432375150L;

  protected JSONObjectString (@Nonnull final IJSONObject aNative) throws JSONCompressException
  {
    super (aNative);
  }

  protected JSONObjectString (@Nonnull final IJSONObject aNative, final boolean bUseZIP) throws JSONCompressException
  {
    super (aNative, bUseZIP);
  }

  protected JSONObjectString (@Nonnull final JSONObjectString aOther)
  {
    super (aOther);
  }

  @Override
  public IJSON getClone ()
  {
    return new JSONObjectString (this);
  }

  @Override
  public IJSONObject getNative () throws JSONParsingException
  {
    return JSONReader.parseObject (getJSONString ());
  }
}
