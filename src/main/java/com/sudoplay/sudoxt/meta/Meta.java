package com.sudoplay.sudoxt.meta;

import com.sudoplay.sudoxt.service.ResourceLocation;
import com.sudoplay.sudoxt.versioning.ArtifactVersion;
import com.sudoplay.sudoxt.versioning.VersionRange;

import java.nio.file.Path;
import java.util.*;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Meta {

  private Path parentPath;
  private Path path;

  private boolean valid;

  private String id;
  private String name;
  private String author;
  private ArtifactVersion version;
  private String description;
  private String website;
  private VersionRange apiVersionRange;

  private Set<String> jarFileSet;
  private Set<Dependency> requiredDependencySet;
  private Set<Dependency> loadAfterDependencySet;
  private Set<Dependency> loadBeforeDependencySet;
  private Map<String, String> registeredPluginMap;
  private Set<String> preloadSet;
  private Map<ResourceLocation, ResourceLocation> overrideMap;

  public Meta(Path parentPath, Path path) {
    this.parentPath = parentPath;
    this.path = path;
    this.valid = true;
    this.requiredDependencySet = new LinkedHashSet<>();
    this.loadAfterDependencySet = new LinkedHashSet<>();
    this.loadBeforeDependencySet = new LinkedHashSet<>();
    this.registeredPluginMap = new LinkedHashMap<>();
    this.preloadSet = new LinkedHashSet<>();
    this.overrideMap = new HashMap<>();
  }

  public void addOverride(ResourceLocation remote, ResourceLocation local) {
    this.overrideMap.put(remote, local);
  }

  public void addPreload(String name) {
    this.preloadSet.add(name);
  }

  public void registerPlugin(String name, String plugin) {
    this.registeredPluginMap.put(name, plugin);
  }

  public Map<String, String> getRegisteredPluginMap() {
    return this.registeredPluginMap;
  }

  public Path getParentPath() {
    return this.parentPath;
  }

  public Path getPath() {
    return this.path;
  }

  public boolean isValid() {
    return this.valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setVersion(ArtifactVersion version) {
    this.version = version;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setApiVersionRange(VersionRange apiVersionRange) {
    this.apiVersionRange = apiVersionRange;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public void setJarFileSet(Set<String> jarFileSet) {
    this.jarFileSet = jarFileSet;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getAuthor() {
    return this.author;
  }

  public ArtifactVersion getVersion() {
    return this.version;
  }

  public String getDescription() {
    return this.description;
  }

  public VersionRange getApiVersionRange() {
    return this.apiVersionRange;
  }

  public String getWebsite() {
    return this.website;
  }

  public Map<ResourceLocation, ResourceLocation> getOverrideMap() {
    return this.overrideMap;
  }

  public void addDependency(Dependency dependency) {
    LoadOrder loadOrder = dependency.getLoadOrder();

    switch (loadOrder) {
      case RequiredBefore:
        this.requiredDependencySet.add(dependency);
        this.loadBeforeDependencySet.add(dependency);
        break;

      case RequiredAfter:
        this.requiredDependencySet.add(dependency);
        this.loadAfterDependencySet.add(dependency);
        break;

      case Before:
        this.loadBeforeDependencySet.add(dependency);
        break;

      case After:
        this.loadAfterDependencySet.add(dependency);
        break;

      default:
        // should never happen
        throw new IllegalArgumentException(String.format("Unrecognized load order enum: %s", loadOrder));
    }
  }

  public Set<Dependency> getRequiredDependencySet() {
    return this.requiredDependencySet;
  }

  public Set<Dependency> getLoadAfterDependencySet() {
    return this.loadAfterDependencySet;
  }

  public Set<Dependency> getLoadBeforeDependencySet() {
    return this.loadBeforeDependencySet;
  }

  public <C extends Collection<Dependency>> C getAllDependencies(C store) {
    store.addAll(this.loadBeforeDependencySet);
    store.addAll(this.loadAfterDependencySet);
    return store;
  }

  public Set<String> getJarFileSet() {
    return this.jarFileSet;
  }

  public Set<String> getPreloadSet() {
    return this.preloadSet;
  }

  @Override
  public String toString() {
    return this.id;
  }
}
