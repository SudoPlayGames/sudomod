package com.sudoplay.sudoext.meta.adapter.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaAdaptException;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class OptionalPreloadAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws Exception {

    if (!jsonObject.has("preload")) {
      return;
    }

    JSONArray jsonArray = jsonObject.getJSONArray("preload");

    for (int i = 0; i < jsonArray.length(); i++) {
      String name = jsonArray.getString(i);

      if (name.isEmpty()) {
        throw new MetaAdaptException(String.format(
            "Array [%s] must contain non-empty strings only, got: '%s'", "preload", name
        ));
      }
      meta.addPreload(name);
    }
  }
}
