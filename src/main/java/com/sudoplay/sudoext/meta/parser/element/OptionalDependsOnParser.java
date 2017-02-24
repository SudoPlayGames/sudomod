package com.sudoplay.sudoext.meta.parser.element;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudoext.meta.*;
import com.sudoplay.sudoext.meta.parser.AbstractMetaElementParser;
import com.sudoplay.sudoext.versioning.VersionRange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalDependsOnParser extends
    AbstractMetaElementParser {

  private static final String VERSION_RANGE_ANY = "[0,)";
  private static final String VERSION_STRING_TEMPLATE = "<instruction>:<id>[@<version>]";

  private class DependencyString {
    String loadOrder;
    String id;
    String versionRange;
  }

  @Override
  public void parse(JsonObject jsonObject, Meta store) throws InvalidMetaException {
    store.setDependencyContainer(this.readDependencyList("depends-on", jsonObject, new DependencyContainer()));
  }

  private DependencyContainer readDependencyList(
      String key,
      JsonObject jsonObject,
      DependencyContainer store
  ) throws InvalidMetaException {
    JsonValue jsonValue = jsonObject.get(key);

    if (jsonValue == null) {
      return store;
    }

    if (!jsonValue.isArray()) {
      throw new InvalidMetaException(String.format("Expected [%s] to be an array, got: %s", key, jsonValue));
    }

    Set<String> dependencyIdSet = new HashSet<>();
    List<DependencyString> dependencyStringList = new ArrayList<>();

    for (JsonValue value : jsonValue.asArray()) {

      if (!value.isString()) {
        throw new InvalidMetaException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, value));
      }

      String dependency = value.asString();

      if (dependency == null || dependency.isEmpty()) {
        throw new InvalidMetaException(String.format("Array [%s] must contain non-empty strings only, got: %s",
            key, dependency));
      }

      DependencyString dependencyString = parseDependencyString(dependency);

      if (dependencyIdSet.contains(dependencyString.id)) {
        throw new InvalidMetaException("Duplicate dependency id: " + dependencyString.id);
      }

      dependencyIdSet.add(dependencyString.id);
      dependencyStringList.add(dependencyString);
    }

    for (DependencyString dependencyString : dependencyStringList) {
      Dependency dependency = createDependency(dependencyString);
      store.addDependency(dependency, dependency.getLoadOrder());
    }

    return store;
  }

  private DependencyString parseDependencyString(String string) throws InvalidMetaException {
    DependencyString data = new DependencyString();
    String[] dependencyAndVersion = string.split("@");

    if (dependencyAndVersion.length > 2 || dependencyAndVersion.length < 1) {
      throw new InvalidMetaException(String.format("Invalid version string: %s, must be " + VERSION_STRING_TEMPLATE,
          string));
    }

    if (dependencyAndVersion.length == 1) {
      // the dependency string doesn't contain a version
      String[] loadOrderAndId = splitIdString(dependencyAndVersion[0]);

      data.loadOrder = loadOrderAndId[0];
      data.id = loadOrderAndId[1];
      data.versionRange = VERSION_RANGE_ANY;

    } else { // two parts: order/id and version
      String[] loadOrderAndId = splitIdString(dependencyAndVersion[0]);
      data.loadOrder = loadOrderAndId[0];
      data.id = loadOrderAndId[1];
      data.versionRange = dependencyAndVersion[1];
    }
    return data;
  }

  private Dependency createDependency(
      DependencyString dependencyString
  ) throws InvalidMetaException {

    LoadOrder loadOrder = readLoadOrder(dependencyString.loadOrder);

    String errorMessage = "Invalid version range: " + dependencyString.versionRange;
    VersionRange versionRange = this.parseVersionRange(dependencyString.versionRange, errorMessage);

    return new Dependency(dependencyString.id, loadOrder, versionRange);
  }

  private String[] splitIdString(String idString) throws InvalidMetaException {
    String[] idSplit = idString.split(":");

    // must have two parts: the load order and id
    if (idSplit.length != 2) {
      throw new InvalidMetaException(String.format("Must be <instruction>:<id>, was %s", idString));
    }
    return idSplit;
  }

  private LoadOrder readLoadOrder(String key) throws InvalidMetaException {
    LoadOrder loadOrder;
    try {
      loadOrder = LoadOrder.from(key);

    } catch (Exception e) {
      throw new InvalidMetaException(e);
    }
    return loadOrder;
  }
}
