package com.sudoplay.sudomod.config;

import java.io.File;

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

  public ConfigBuilder setModDataLocation(File modDataLocation) {
    this.config.setModDataLocation(modDataLocation);
    return this;
  }

  public ConfigBuilder setModLocation(File modLocation) {
    this.config.setModLocation(modLocation);
    return this;
  }

  public ConfigBuilder setModInfoFilename(String modInfoFilename) {
    this.config.setModInfoFilename(modInfoFilename);
    return this;
  }

  public ConfigBuilder setDefaultApiVersionString(String defaultApiVersionString) {
    this.config.setDefaultApiVersionString(defaultApiVersionString);
    return this;
  }

  public Config getConfig() {
    return this.config;
  }
}
