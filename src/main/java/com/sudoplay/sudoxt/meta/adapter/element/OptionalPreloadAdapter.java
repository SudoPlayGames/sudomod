package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.json.JSONArray;
import com.sudoplay.json.JSONObject;

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
