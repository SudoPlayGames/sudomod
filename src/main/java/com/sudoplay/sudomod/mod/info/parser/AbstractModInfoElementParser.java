package com.sudoplay.sudomod.mod.info.parser;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.versioning.InvalidVersionSpecificationException;
import com.sudoplay.sudomod.versioning.VersionRange;

/**
 * Created by codetaylor on 2/18/2017.
 */
public abstract class AbstractModInfoElementParser implements
    IModInfoElementParser {

  protected String readString(String key, JsonObject jsonObject) throws InvalidModInfoException {
    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      throw new InvalidModInfoException(String.format("Mod info json missing [%s]", key));
    }

    if (!jsonValue.isString()) {
      throw new InvalidModInfoException(String.format("Expected mod info json [%s] to be a string, got %s", key,
          jsonValue));
    }

    return jsonValue.asString();
  }

  /**
   * @param key        the string key
   * @param jsonObject the object to read from
   * @return the string or an empty string if it doesn't exist
   * @throws InvalidModInfoException
   */
  protected String readOptionalString(String key, JsonObject jsonObject) throws InvalidModInfoException {
    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      return "";
    }

    if (!jsonValue.isString()) {
      throw new InvalidModInfoException(String.format("Expected mod info json [%s] to be a string, got %s", key,
          jsonValue));
    }

    return jsonValue.asString();
  }

  protected VersionRange parseVersionRange(String versionString, String errorMessage) throws InvalidModInfoException {
    VersionRange versionRange;

    try {
      versionRange = VersionRange.createFromVersionSpec(versionString);

    } catch (InvalidVersionSpecificationException e) {
      throw new InvalidModInfoException(errorMessage, e);
    }
    return versionRange;
  }
}
