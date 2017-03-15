package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class OptionalRegisterAdapter implements
    IMetaAdapter {

  private boolean autoPreloadRegisteredPlugins;

  public OptionalRegisterAdapter(boolean autoPreloadRegisteredPlugins) {
    this.autoPreloadRegisteredPlugins = autoPreloadRegisteredPlugins;
  }

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws Exception {

    if (!jsonObject.has("register")) {
      return;
    }

    JSONObject registerObject = jsonObject.getJSONObject("register");

    for (Iterator<String> it = registerObject.keys(); it.hasNext(); ) {
      String key = it.next();

      String registerObjectString = registerObject.getString(key);
      meta.registerPlugin(key, registerObjectString);

      if (this.autoPreloadRegisteredPlugins) {
        meta.addPreload(registerObjectString);
      }
    }
  }
}
