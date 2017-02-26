package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.*;
import com.sudoplay.sudoext.meta.parser.AbstractMetaElementParser;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    store.setDependencyContainer(this.readDependencyList("depends-on", jsonObject, new DependencyContainer()));
  }

  private DependencyContainer readDependencyList(
      String key,
      JSONObject jsonObject,
      DependencyContainer store
  ) throws MetaParseException {

    JSONArray jsonArray;
    Set<String> dependencyIdSet;
    List<DependencyString> dependencyStringList;

    if (!jsonObject.has(key)) {
      // return an empty dependency container
      return store;
    }

    try {
      jsonArray = jsonObject.getJSONArray(key);

    } catch (JSONException e) {
      throw new MetaParseException(String.format("Expected [%s] to be an array, got: %s", key, jsonObject.get(key)), e);
    }

    dependencyIdSet = new HashSet<>();
    dependencyStringList = new ArrayList<>();

    for (Object value : jsonArray.toList()) {

      if (!(value instanceof String) || ((String) value).isEmpty()) {
        throw new MetaParseException(String.format(
            "Array [%s] must contain non-empty strings only, got: %s", key, value
        ));
      }

      DependencyString dependencyString = parseDependencyString((String) value);

      if (dependencyIdSet.contains(dependencyString.id)) {
        throw new MetaParseException("Duplicate dependency id: " + dependencyString.id);
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

  private DependencyString parseDependencyString(String string) throws MetaParseException {
    DependencyString data = new DependencyString();
    String[] dependencyAndVersion = string.split("@");

    if (dependencyAndVersion.length > 2 || dependencyAndVersion.length < 1) {
      throw new MetaParseException(String.format("Invalid version string: %s, must be " + VERSION_STRING_TEMPLATE,
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
  ) throws MetaParseException {

    LoadOrder loadOrder = readLoadOrder(dependencyString.loadOrder);

    String errorMessage = "Invalid version range: " + dependencyString.versionRange;
    VersionRange versionRange = this.parseVersionRange(dependencyString.versionRange, errorMessage);

    return new Dependency(dependencyString.id, loadOrder, versionRange);
  }

  private String[] splitIdString(String idString) throws MetaParseException {
    String[] idSplit = idString.split(":");

    // must have two parts: the load order and id
    if (idSplit.length != 2) {
      throw new MetaParseException(String.format("Must be <instruction>:<id>, was %s", idString));
    }
    return idSplit;
  }

  private LoadOrder readLoadOrder(String key) throws MetaParseException {
    LoadOrder loadOrder;
    try {
      loadOrder = LoadOrder.from(key);

    } catch (Exception e) {
      throw new MetaParseException(e);
    }
    return loadOrder;
  }
}
