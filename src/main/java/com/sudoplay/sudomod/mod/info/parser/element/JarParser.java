package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.parser.AbstractElementParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class JarParser extends
    AbstractElementParser {

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setJarFileList(this.readJarFileList("jars", jsonObject, new ArrayList<>()));
  }

  private List<String> readJarFileList(String key, JsonObject jsonObject, List<String> store) throws
      InvalidModInfoException {

    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      return store;
    }

    if (!jsonValue.isArray()) {
      throw new InvalidModInfoException(String.format("Expected [%s] to be an array, got: %s", key, jsonValue));
    }

    for (JsonValue value : jsonValue.asArray()) {

      if (!value.isString()) {
        throw new InvalidModInfoException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, value));
      }

      String jarFileString = value.asString();

      if (jarFileString == null || jarFileString.isEmpty()) {
        throw new InvalidModInfoException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, jarFileString));
      }

      store.add(jarFileString);
    }

    return store;
  }

}
