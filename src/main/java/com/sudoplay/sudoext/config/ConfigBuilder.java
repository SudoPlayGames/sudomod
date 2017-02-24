package com.sudoplay.sudoext.config;

import com.sudoplay.sudoext.classloader.intercept.ClassIntercept;
import com.sudoplay.sudoext.container.IContainerCacheFactory;
import com.sudoplay.sudoext.container.IContainerSorter;
import com.sudoplay.sudoext.meta.IMetaFactory;
import com.sudoplay.sudoext.security.IClassFilter;
import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;

import java.nio.file.Path;

/**
 * Configuration context builder.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class ConfigBuilder {

  private Config config;

  public ConfigBuilder() {
    this.config = new Config();
  }

  public ConfigBuilder setDataLocation(Path dataLocation) {
    this.config.dataLocation = dataLocation;
    return this;
  }

  public ConfigBuilder setLocation(Path location) {
    this.config.location = location;
    return this;
  }

  public ConfigBuilder setMetaFilename(String filename) {
    this.config.metaFilename = filename;
    return this;
  }

  /**
   * This is the default version string used when one isn't supplied in the meta file.
   *
   * @param defaultApiVersionString the default api version string
   * @return this builder
   */
  public ConfigBuilder setDefaultMetaApiVersionString(String defaultApiVersionString) {
    this.config.defaultMetaApiVersionString = defaultApiVersionString;
    return this;
  }

  public ConfigBuilder setFollowLinks(boolean followLinks) {
    this.config.followLinks = followLinks;
    return this;
  }

  public ConfigBuilder setTempLocation(Path tempLocation) {
    this.config.tempLocation = tempLocation;
    return this;
  }

  public ConfigBuilder setCompressedFileExtension(String compressedFileExtension) {
    this.config.compressedFileExtension = compressedFileExtension;
    return this;
  }

  public ConfigBuilder setApiVersion(String apiVersionString) {
    this.config.apiVersion = new DefaultArtifactVersion(apiVersionString);
    return this;
  }

  public ConfigBuilder addClassFilter(IClassFilter classFilters) {
    this.config.classFilterList.add(classFilters);
    return this;
  }

  public ConfigBuilder setContainerCacheFactory(IContainerCacheFactory containerCacheFactory) {
    this.config.containerCacheFactory = containerCacheFactory;
    return this;
  }

  public ConfigBuilder addClassIntercept(ClassIntercept classIntercept) {
    this.config.classInterceptList.add(classIntercept);
    return this;
  }

  public ConfigBuilder setContainerSorter(IContainerSorter containerSorter) {
    this.config.containerSorter = containerSorter;
    return this;
  }

  public ConfigBuilder setMetaFactory(IMetaFactory metaFactory) {
    this.config.metaFactory = metaFactory;
    return this;
  }

  public Config getConfig() {
    return this.config;
  }
}
