package com.phloc.json.impl;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.zip.GZIPUtils;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONString;

public abstract class AbstractJSONString <T extends IJSON> extends AbstractJSON implements IJSONString <T>
{
  private static final long serialVersionUID = 9016882425432375150L;
  private static final Logger LOG = LoggerFactory.getLogger (AbstractJSONString.class);
  private static final boolean DEFAULT_USE_ZIP = true;
  private String m_sJSONString;
  private byte [] m_sJSONStringZipped;
  private boolean m_bZipped;

  protected AbstractJSONString (@Nonnull final T aNative) throws JSONCompressException
  {
    this (aNative, DEFAULT_USE_ZIP);
  }

  protected AbstractJSONString (@Nonnull final T aNative, final boolean bUseZIP) throws JSONCompressException
  {
    ValueEnforcer.notNull (aNative, "aNative"); //$NON-NLS-1$
    final String sUnzipped = aNative.getJSONString (false);
    this.m_bZipped = bUseZIP;
    if (this.m_bZipped)
    {
      this.m_sJSONString = null;
      try
      {
        this.m_sJSONStringZipped = GZIPUtils.compress (sUnzipped);
      }
      catch (final IOException aEx)
      {
        throw new JSONCompressException ("Failed to zip JSON", aEx); //$NON-NLS-1$
      }
    }
    else
    {
      this.m_sJSONString = sUnzipped;
      this.m_sJSONStringZipped = null;
    }
  }

  protected AbstractJSONString (@Nonnull final AbstractJSONString <T> aOther)
  {
    ValueEnforcer.notNull (aOther, "aOther"); //$NON-NLS-1$
    this.m_sJSONString = aOther.m_sJSONString;
    this.m_sJSONStringZipped = ArrayHelper.getCopy (aOther.m_sJSONStringZipped);
    this.m_bZipped = aOther.m_bZipped;
  }

  @Override
  public void appendJSONString (final StringBuilder aResult, final boolean bAlignAndIndent, final int nLevel)
  {
    if (bAlignAndIndent && LOG.isDebugEnabled ())
    {
      LOG.debug ("Indentation and alignment is not supported for JSONString for the sake of memory efficiency!"); //$NON-NLS-1$
    }
    try
    {
      aResult.append (this.m_bZipped ? GZIPUtils.decompress (this.m_sJSONStringZipped) : this.m_sJSONString);
    }
    catch (final IOException aEx)
    {
      throw new IllegalStateException ("Failed to unzip JSON", aEx); //$NON-NLS-1$
    }

  }

  @Override
  public boolean equals (final Object aOther)
  {
    if (aOther == this)
    {
      return true;
    }
    if (!super.equals (aOther))
    {
      return false;
    }
    final AbstractJSONString <?> rhs = (AbstractJSONString <?>) aOther;
    return EqualsUtils.equals (this.m_sJSONString, rhs.m_sJSONString) &&
           EqualsUtils.equals (this.m_bZipped, rhs.m_bZipped);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (this.m_sJSONString)
                            .append (this.m_bZipped)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("jsonstring", this.m_sJSONString) //$NON-NLS-1$
                            .append ("zipped", this.m_bZipped) //$NON-NLS-1$
                            .toString ();
  }
}
