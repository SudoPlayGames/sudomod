package com.sudoplay.sudomod.config;

import com.sudoplay.sudomod.mod.security.IClassFilter;
import com.sudoplay.sudomod.versioning.DefaultArtifactVersion;

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

  public ConfigBuilder setModDataLocation(Path modDataLocation) {
    this.config.modDataLocation = modDataLocation;
    return this;
  }

  public ConfigBuilder setModLocation(Path modLocation) {
    this.config.modLocation = modLocation;
    return this;
  }

  public ConfigBuilder setModInfoFilename(String modInfoFilename) {
    this.config.modInfoFilename = modInfoFilename;
    return this;
  }

  /**
   * This is the default version string used when one isn't supplied in the mod info file.
   *
   * @param defaultApiVersionString the default api version string
   * @return this builder
   */
  public ConfigBuilder setDefaultModInfoApiVersionString(String defaultApiVersionString) {
    this.config.defaultModInfoApiVersionString = defaultApiVersionString;
    return this;
  }

  public ConfigBuilder setFollowLinks(boolean followLinks) {
    this.config.followLinks = followLinks;
    return this;
  }

  public ConfigBuilder setModTempLocation(Path modTempLocation) {
    this.config.modTempLocation = modTempLocation;
    return this;
  }

  public ConfigBuilder setCompressedModFileExtension(String compressedModFileExtension) {
    this.config.compressedModFileExtension = compressedModFileExtension;
    return this;
  }

  public ConfigBuilder setApiVersion(String apiVersionString) {
    this.config.apiVersion = new DefaultArtifactVersion(apiVersionString);
    return this;
  }

  public ConfigBuilder setClassFilters(IClassFilter[] classFilters) {
    this.config.classFilters = classFilters;
    return this;
  }

  public Config getConfig() {
    return this.config;
  }
}
