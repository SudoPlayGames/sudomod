package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoxt.service.SXResourceLocation;
import com.sudoplay.sudoxt.service.SXResourceStringParseException;
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
      JSONObject modOverride = override.getJSONObject(key);

      for (Iterator<String> itt = modOverride.keys(); itt.hasNext(); ) {
        String remoteResource = itt.next();
        String localResource = modOverride.getString(remoteResource);

        if (remoteResource == null || remoteResource.isEmpty()) {
          throw new MetaAdaptException(String.format(
              "Override keys must be non-empty strings, got: '%s'", remoteResource
          ));
        }

        if (localResource == null || localResource.isEmpty()) {
          throw new MetaAdaptException(String.format(
              "Override values must be non-empty strings, got: '%s'", localResource
          ));
        }

        try {
          SXResourceLocation remoteResourceLocation = new SXResourceLocation(key + ":" + remoteResource);
          SXResourceLocation localResourceLocation = new SXResourceLocation(meta.getId() + ":" + localResource);
          meta.addOverride(remoteResourceLocation, localResourceLocation);

        } catch (SXResourceStringParseException e) {
          throw new MetaAdaptException(String.format("Invalid override key [%s]", key), e);
        }
      }
    }
  }
}
