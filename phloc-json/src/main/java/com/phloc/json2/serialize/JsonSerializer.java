package com.phloc.json2.serialize;

import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.json2.IJson;
import com.phloc.json2.IJsonArray;
import com.phloc.json2.IJsonObject;
import com.phloc.json2.IJsonValue;

public class JsonSerializer
{
  public static String getAsString (@Nonnull final IJson aJson)
  {
    if (aJson == null)
      throw new NullPointerException ("JSON");

    if (aJson.isValue ())
    {
      final IJsonValue aJsonValue = (IJsonValue) aJson;
      return aJsonValue.getValueSerializer ().getAsJsonString (aJsonValue.getValue ());
    }

    if (aJson.isArray ())
    {
      final StringBuilder aSB = new StringBuilder ().append ('[');
      int nIndex = 0;
      for (final IJson aChild : (IJsonArray) aJson)
      {
        if (nIndex++ > 0)
          aSB.append (',');
        aSB.append (getAsString (aChild));
      }
      return aSB.append (']').toString ();
    }

    if (aJson.isObject ())
    {
      final StringBuilder aSB = new StringBuilder ().append ('{');
      int nIndex = 0;
      for (final Map.Entry <String, IJson> aEntry : (IJsonObject) aJson)
      {
        if (nIndex++ > 0)
          aSB.append (',');
        aSB.append (JsonValueSerializerEscaped.getEscapedJsonString (aEntry.getKey ()))
           .append (':')
           .append (getAsString (aEntry.getValue ()));
      }
      return aSB.append ('}').toString ();
    }

    throw new IllegalArgumentException ("Unsupported Json Object type: " + aJson);
  }
}
