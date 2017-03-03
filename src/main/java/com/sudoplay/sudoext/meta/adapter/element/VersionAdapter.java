package com.sudoplay.sudoext.meta.adapter.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Reads version from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class VersionAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws JSONException {
    meta.setVersion(new DefaultArtifactVersion(jsonObject.getString("version")));
  }
}
