package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.parser.AbstractModInfoElementParser;
import com.sudoplay.sudomod.versioning.VersionRange;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ApiVersionRangeParser extends
    AbstractModInfoElementParser {

  private String defaultApiVersionString;

  public ApiVersionRangeParser(String defaultApiVersionString) {
    this.defaultApiVersionString = defaultApiVersionString;
  }

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setApiVersionRange(this.readApiVersionRange("api-version", jsonObject));
  }

  private VersionRange readApiVersionRange(String key, JsonObject jsonObject) throws InvalidModInfoException {
    String apiVersionString = this.readOptionalString(key, jsonObject);

    if (apiVersionString.isEmpty()) {
      apiVersionString = this.defaultApiVersionString;
    }

    return this.parseVersionRange(apiVersionString, "Invalid api version string: " + apiVersionString);
  }
}
