package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudomod.mod.info.*;
import com.sudoplay.sudomod.mod.info.parser.AbstractElementParser;
import com.sudoplay.sudomod.versioning.VersionRange;

import java.util.Arrays;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class DependsOnParser extends
    AbstractElementParser {

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setModDependencyContainer(this.readModDependencyList("depends-on", jsonObject, new ModDependencyContainer()));
  }

  private ModDependencyContainer readModDependencyList(
      String key,
      JsonObject jsonObject,
      ModDependencyContainer store
  ) throws InvalidModInfoException {
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

      String dependency = value.asString();

      if (dependency == null || dependency.isEmpty()) {
        throw new InvalidModInfoException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, dependency));
      }

      parseDependencyString(dependency, store);
    }

    return store;
  }

  private void parseDependencyString(String dependency, ModDependencyContainer store) throws InvalidModInfoException {
    String[] split = dependency.split("@");

    if (split.length > 2 || split.length < 1) {
      throw new InvalidModInfoException(String.format("Invalid version string: %s", dependency));
    }

    if (split.length == 1) {
      // the dependency string doesn't contain a version
      ModDependency modDependency = this.parseModDependency(dependency, split[0], "[0.0,)");
      store.addModDependency(modDependency, modDependency.getLoadOrder());

    } else { // two parts: order/id and version
      ModDependency modDependency = this.parseModDependency(dependency, split[0], split[1]);
      store.addModDependency(modDependency, modDependency.getLoadOrder());
    }
  }

  private ModDependency parseModDependency(
      String dependency,
      String idString,
      String versionString
  ) throws InvalidModInfoException {

    String[] idSplit = splitIdString(dependency, idString);

    String id = idSplit[1];
    LoadOrder loadOrder = readLoadOrder(dependency, idSplit[0]);

    String errorMessage = "Invalid dependency version string: " + dependency;
    VersionRange versionRange = parseVersionRange(versionString, errorMessage);

    return new ModDependency(id, loadOrder, versionRange);
  }

  private String[] splitIdString(String dependency, String idString) throws InvalidModInfoException {
    String[] idSplit = idString.split(":");

    // must have two parts: the load order and mod id
    if (idSplit.length != 2) {
      throw new InvalidModInfoException(String.format("Invalid version string: %s", dependency));
    }
    return idSplit;
  }

  private LoadOrder readLoadOrder(String dependency, String key) throws InvalidModInfoException {
    LoadOrder loadOrder;
    try {
      loadOrder = LoadOrder.from(key);

    } catch (Exception e) {
      throw new InvalidModInfoException(String.format("Invalid version string: %s", dependency), e);
    }
    return loadOrder;
  }
}
