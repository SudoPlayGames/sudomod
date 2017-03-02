package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Reads version from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class VersionParser implements
    IMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws JSONException {
    store.setVersion(new DefaultArtifactVersion(jsonObject.getString("version")));
  }
}
