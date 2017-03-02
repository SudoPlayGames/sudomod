package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Reads jar from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalJarParser implements
    IMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException, JSONException {
    store.setJarFileSet(this.readJarFileList("jars", jsonObject, new LinkedHashSet<>()));
  }

  private Set<String> readJarFileList(String key, JSONObject jsonObject, Set<String> store) throws
      MetaParseException, JSONException {

    if (!jsonObject.has(key)) {
      return store;
    }

    JSONArray jsonArray;

    jsonArray = jsonObject.getJSONArray(key);

    for (int i = 0; i < jsonArray.length(); i++) {

      Object value;

      value = jsonArray.getString(i);

      if (value == null || ((String) value).isEmpty()) {
        throw new MetaParseException(String.format(
            "Array [%s] must contain non-empty strings only, got: %s", key, value
        ));
      }

      if (store.contains(value)) {
        throw new MetaParseException(String.format("Duplicate jar file [%s]", value));
      }

      store.add((String) value);
    }

    return store;
  }

}
