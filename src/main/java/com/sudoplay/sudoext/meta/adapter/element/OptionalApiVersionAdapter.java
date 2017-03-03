package com.sudoplay.sudoext.meta.adapter.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaAdaptException;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoext.versioning.InvalidVersionSpecificationException;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.json.JSONObject;

/**
 * Reads api version from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalApiVersionAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws MetaAdaptException {
    meta.setApiVersionRange(this.readApiVersionRange("api-version", jsonObject));
  }

  private VersionRange readApiVersionRange(String key, JSONObject jsonObject) throws MetaAdaptException {
    String versionString = jsonObject.optString(key, "[0,)");

    try {
      return VersionRange.createFromVersionSpec(versionString);

    } catch (InvalidVersionSpecificationException e) {
      throw new MetaAdaptException("Invalid api version string: " + versionString, e);
    }
  }
}
