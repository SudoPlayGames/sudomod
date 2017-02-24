package com.sudoplay.sudoext.meta.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudoext.meta.InvalidMetaException;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.AbstractElementParser;
import com.sudoplay.sudoext.versioning.VersionRange;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ApiVersionRangeParser extends
    AbstractElementParser {

  private String defaultApiVersionString;

  public ApiVersionRangeParser(String defaultApiVersionString) {
    this.defaultApiVersionString = defaultApiVersionString;
  }

  @Override
  public void parse(JsonObject jsonObject, Meta store) throws InvalidMetaException {
    store.setApiVersionRange(this.readApiVersionRange("api-version", jsonObject));
  }

  private VersionRange readApiVersionRange(String key, JsonObject jsonObject) throws InvalidMetaException {
    String apiVersionString = this.readOptionalString(key, jsonObject);

    if (apiVersionString.isEmpty()) {
      apiVersionString = this.defaultApiVersionString;
    }

    return this.parseVersionRange(apiVersionString, "Invalid api version string: " + apiVersionString);
  }
}
