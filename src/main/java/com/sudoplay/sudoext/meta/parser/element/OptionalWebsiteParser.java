package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import org.json.JSONObject;

/**
 * Reads website form meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalWebsiteParser implements
    IMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    store.setWebsite(jsonObject.optString("website", ""));
  }
}
