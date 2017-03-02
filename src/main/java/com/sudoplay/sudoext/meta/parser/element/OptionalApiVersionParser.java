package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import com.sudoplay.sudoext.versioning.InvalidVersionSpecificationException;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.json.JSONObject;

/**
 * Reads api version from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalApiVersionParser implements
    IMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    store.setApiVersionRange(this.readApiVersionRange("api-version", jsonObject));
  }

  private VersionRange readApiVersionRange(String key, JSONObject jsonObject) throws MetaParseException {
    String versionString = jsonObject.optString(key, "[0,)");

    try {
      return VersionRange.createFromVersionSpec(versionString);

    } catch (InvalidVersionSpecificationException e) {
      throw new MetaParseException("Invalid api version string: " + versionString, e);
    }
  }
}
