package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.*;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import com.sudoplay.sudoext.util.StringUtil;
import com.sudoplay.sudoext.versioning.InvalidVersionSpecificationException;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reads optional depends-on from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalDependsOnParser implements
    IMetaElementParser {

  private static final String VERSION_RANGE_ANY = "[0,)";
  private static final String VERSION_STRING_TEMPLATE = "<instruction>:<id>[@<version>]";

  /**
   * Simple data object used in this class only.
   */
  private class DependencyString {

    String loadOrder;
    String id;
    String versionRange;
  }

  public interface IDependencyContainerFactory {
    DependencyContainer create();
  }

  private IDependencyContainerFactory dependencyContainerFactory;

  public OptionalDependsOnParser(IDependencyContainerFactory dependencyContainerFactory) {
    this.dependencyContainerFactory = dependencyContainerFactory;
  }

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException, JSONException {
    store.setDependencyContainer(this.readDependencyList(
        "depends-on",
        jsonObject,
        this.dependencyContainerFactory.create())
    );
  }

  private DependencyContainer readDependencyList(
      String key,
      JSONObject jsonObject,
      DependencyContainer store
  ) throws MetaParseException, JSONException {

    JSONArray jsonArray;
    Set<String> dependencyIdSet;
    List<DependencyString> dependencyStringList;

    if (!jsonObject.has(key)) {
      // return an empty dependency container
      return store;
    }

    jsonArray = jsonObject.getJSONArray(key);

    dependencyIdSet = new HashSet<>();
    dependencyStringList = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {

      Object value;

      value = jsonArray.getString(i);

      if (value == null || ((String) value).isEmpty()) {
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

      Dependency dependency;

      dependency = new Dependency(
          dependencyString.id,
          this.readLoadOrder(dependencyString.loadOrder),
          this.readVersionRange(dependencyString.versionRange)
      );

      store.addDependency(dependency, dependency.getLoadOrder());
    }

    return store;
  }

  @NotNull
  private DependencyString parseDependencyString(@NotNull String string) throws MetaParseException {

    if (StringUtil.countOccurrences(string, '@') > 1) {
      throw new MetaParseException(String.format(
          "Invalid version string: %s, must be " + VERSION_STRING_TEMPLATE, string)
      );
    }

    if (StringUtil.countOccurrences(string, ':') != 1) {
      throw new MetaParseException(String.format(
          "Invalid version string: %s, must be " + VERSION_STRING_TEMPLATE, string)
      );
    }

    DependencyString data = new DependencyString();
    String[] dependencyAndVersion = string.split("@");

    if (dependencyAndVersion.length == 1) {
      // the dependency string doesn't contain a version
      String[] loadOrderAndId = this.splitIdString(dependencyAndVersion[0]);

      data.loadOrder = loadOrderAndId[0];
      data.id = loadOrderAndId[1];
      data.versionRange = VERSION_RANGE_ANY;

    } else { // two parts: order/id and version
      String[] loadOrderAndId = this.splitIdString(dependencyAndVersion[0]);
      data.loadOrder = loadOrderAndId[0];
      data.id = loadOrderAndId[1];
      data.versionRange = dependencyAndVersion[1];
    }
    return data;
  }

  /**
   * Splits a string of the form '<instruction>:<id>' and returns a string array containing each side.
   *
   * @param idString
   * @return
   * @throws MetaParseException
   */
  @NotNull
  private String[] splitIdString(@NotNull String idString) throws MetaParseException {
    String[] idSplit = idString.split(":");

    // must have two parts: the load order and id
    if (idSplit.length != 2) {
      throw new MetaParseException(String.format("Must be <instruction>:<id>, was %s", idString));
    }
    return idSplit;
  }

  @NotNull
  private LoadOrder readLoadOrder(@NotNull String key) throws MetaParseException {

    try {
      return LoadOrder.from(key);

    } catch (Exception e) {
      throw new MetaParseException(e);
    }
  }

  @NotNull
  private VersionRange readVersionRange(@NotNull String versionRangeString) throws MetaParseException {

    try {
      return VersionRange.createFromVersionSpec(versionRangeString);

    } catch (InvalidVersionSpecificationException e) {
      throw new MetaParseException("Invalid version range: " + versionRangeString, e);
    }
  }
}
