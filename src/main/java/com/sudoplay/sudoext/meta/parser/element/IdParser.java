package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Reads id from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class IdParser implements
    IMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws JSONException {
    store.setId(jsonObject.getString("id"));
  }
}
