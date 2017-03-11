package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
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
public class OptionalJarAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws MetaAdaptException, JSONException {
    meta.setJarFileSet(this.readJarFileList("jars", jsonObject, new LinkedHashSet<>()));
  }

  private Set<String> readJarFileList(String key, JSONObject jsonObject, Set<String> store) throws
      MetaAdaptException, JSONException {

    if (!jsonObject.has(key)) {
      return store;
    }

    JSONArray jsonArray;

    jsonArray = jsonObject.getJSONArray(key);

    for (int i = 0; i < jsonArray.length(); i++) {

      String value;

      value = jsonArray.getString(i);

      if (value == null || value.isEmpty()) {
        throw new MetaAdaptException(String.format(
            "Array [%s] must contain non-empty strings only, got: '%s'", key, value
        ));
      }

      if (store.contains(value)) {
        throw new MetaAdaptException(String.format("Duplicate jar file [%s]", value));
      }

      store.add(value);
    }

    return store;
  }

}
