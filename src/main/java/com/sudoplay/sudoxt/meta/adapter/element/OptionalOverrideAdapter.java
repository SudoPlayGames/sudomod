package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoxt.service.ResourceLocation;
import com.sudoplay.sudoxt.service.ResourceStringParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class OptionalOverrideAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws Exception {

    if (!jsonObject.has("override")) {
      return;
    }

    JSONObject override = jsonObject.getJSONObject("override");

    for (Iterator<String> it = override.keys(); it.hasNext(); ) {

      String key = it.next();
      JSONArray value = override.getJSONArray(key);

      for (int i = 0; i < value.length(); i++) {
        String resource = value.getString(i);

        if (resource == null || resource.isEmpty()) {
          throw new MetaAdaptException(String.format(
              "Array [%s] must contain non-empty strings only, got: '%s'", key, resource
          ));
        }

        try {
          ResourceLocation remoteResourceLocation = new ResourceLocation(key + ":" + resource);
          ResourceLocation localResourceLocation = new ResourceLocation(meta.getId() + ":" + resource);
          meta.addOverride(remoteResourceLocation, localResourceLocation);

        } catch (ResourceStringParseException e) {
          throw new MetaAdaptException(String.format("Invalid override key [%s]", key), e);
        }
      }
    }
  }
}
