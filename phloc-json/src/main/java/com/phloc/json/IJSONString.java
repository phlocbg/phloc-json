package com.phloc.json;

import com.phloc.json.impl.JSONParsingException;

public interface IJSONString <T extends IJSON> extends IJSON
{
  T getNative () throws JSONParsingException;
}
