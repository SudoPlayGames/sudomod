package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.parser.AbstractMetaElementParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalJarParser extends
    AbstractMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    store.setJarFileList(this.readJarFileList("jars", jsonObject, new ArrayList<>()));
  }

  private List<String> readJarFileList(String key, JSONObject jsonObject, List<String> store) throws
      MetaParseException {

    if (!jsonObject.has(key)) {
      return store;
    }

    JSONArray jsonArray;

    try {
      jsonArray = jsonObject.getJSONArray(key);

    } catch (JSONException e) {
      throw new MetaParseException(String.format("Expected [%s] to be an array, got: %s", key, jsonObject.get(key)), e);
    }

    for (Object value : jsonArray.toList()) {

      if (!(value instanceof String) || ((String) value).isEmpty()) {
        throw new MetaParseException(String.format(
            "Array [%s] must contain non-empty strings only, got: %s", key, value
        ));
      }

      store.add((String) value);
    }

    return store;
  }

}
