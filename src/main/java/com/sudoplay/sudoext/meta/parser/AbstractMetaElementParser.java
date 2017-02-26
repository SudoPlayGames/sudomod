package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.versioning.InvalidVersionSpecificationException;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by codetaylor on 2/18/2017.
 */
public abstract class AbstractMetaElementParser implements
    IMetaElementParser {

  @NotNull
  protected String readString(
      @NotNull String key,
      @NotNull JSONObject jsonObject
  ) throws MetaParseException {

    try {
      return jsonObject.getString(key);

    } catch (JSONException e) {
      throw new MetaParseException(String.format("JSON missing string for key [%s]", key), e);
    }
  }

  protected VersionRange parseVersionRange(String versionString, String errorMessage) throws MetaParseException {
    VersionRange versionRange;

    try {
      versionRange = VersionRange.createFromVersionSpec(versionString);

    } catch (InvalidVersionSpecificationException e) {
      throw new MetaParseException(errorMessage, e);
    }
    return versionRange;
  }
}
