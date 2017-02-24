package com.sudoplay.sudoext.meta.parser;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudoext.meta.InvalidMetaException;
import com.sudoplay.sudoext.versioning.InvalidVersionSpecificationException;
import com.sudoplay.sudoext.versioning.VersionRange;

/**
 * Created by codetaylor on 2/18/2017.
 */
public abstract class AbstractMetaElementParser implements
    IMetaElementParser {

  protected String readString(String key, JsonObject jsonObject) throws InvalidMetaException {
    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      throw new InvalidMetaException(String.format("Meta json missing [%s]", key));
    }

    if (!jsonValue.isString()) {
      throw new InvalidMetaException(String.format("Expected meta json [%s] to be a string, got %s", key,
          jsonValue));
    }

    return jsonValue.asString();
  }

  /**
   * @param key        the string key
   * @param jsonObject the object to read from
   * @return the string or an empty string if it doesn't exist
   * @throws InvalidMetaException
   */
  protected String readOptionalString(String key, JsonObject jsonObject) throws InvalidMetaException {
    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      return "";
    }

    if (!jsonValue.isString()) {
      throw new InvalidMetaException(String.format("Expected meta json [%s] to be a string, got %s", key,
          jsonValue));
    }

    return jsonValue.asString();
  }

  protected VersionRange parseVersionRange(String versionString, String errorMessage) throws InvalidMetaException {
    VersionRange versionRange;

    try {
      versionRange = VersionRange.createFromVersionSpec(versionString);

    } catch (InvalidVersionSpecificationException e) {
      throw new InvalidMetaException(errorMessage, e);
    }
    return versionRange;
  }
}
