package com.sudoplay.sudoext.meta.parser.element;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudoext.meta.InvalidMetaException;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.AbstractMetaElementParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalJarParser extends
    AbstractMetaElementParser {

  @Override
  public void parse(JsonObject jsonObject, Meta store) throws InvalidMetaException {
    store.setJarFileList(this.readJarFileList("jars", jsonObject, new ArrayList<>()));
  }

  private List<String> readJarFileList(String key, JsonObject jsonObject, List<String> store) throws
      InvalidMetaException {

    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      return store;
    }

    if (!jsonValue.isArray()) {
      throw new InvalidMetaException(String.format("Expected [%s] to be an array, got: %s", key, jsonValue));
    }

    for (JsonValue value : jsonValue.asArray()) {

      if (!value.isString()) {
        throw new InvalidMetaException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, value));
      }

      String jarFileString = value.asString();

      if (jarFileString == null || jarFileString.isEmpty()) {
        throw new InvalidMetaException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, jarFileString));
      }

      store.add(jarFileString);
    }

    return store;
  }

}
