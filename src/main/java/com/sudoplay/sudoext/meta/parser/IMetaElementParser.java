package com.sudoplay.sudoext.meta.parser;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudoext.meta.InvalidMetaException;
import com.sudoplay.sudoext.meta.Meta;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaElementParser {

  void parse(JsonObject jsonObject, Meta store) throws InvalidMetaException;

}
