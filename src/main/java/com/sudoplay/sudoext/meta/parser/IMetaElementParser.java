package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.Meta;
import org.json.JSONObject;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaElementParser {

  void parse(JSONObject jsonObject, Meta store) throws MetaParseException;
}
