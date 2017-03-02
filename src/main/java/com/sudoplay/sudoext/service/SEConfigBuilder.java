package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Configuration context builder.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class SEConfigBuilder {

  private SEConfig config;

  public SEConfigBuilder() {
    this.config = new SEConfig();
  }

  public SEConfigBuilder setDataLocation(Path dataLocation) {
    this.config.dataLocation = dataLocation;
    return this;
  }

  public SEConfigBuilder setLocation(Path location) {
    this.config.location = location;
    return this;
  }

  public SEConfigBuilder setMetaFilename(String filename) {
    this.config.metaFilename = filename;
    return this;
  }

  public SEConfigBuilder setTempLocation(Path tempLocation) {
    this.config.tempLocation = tempLocation;
    return this;
  }

  public SEConfigBuilder setCompressedFileExtension(String compressedFileExtension) {
    this.config.compressedFileExtension = compressedFileExtension;
    return this;
  }

  public SEConfigBuilder setApiVersion(String apiVersionString) {
    this.config.apiVersion = new DefaultArtifactVersion(apiVersionString);
    return this;
  }

  public SEConfigBuilder setCharset(Charset charset) {
    this.config.charset = charset;
    return this;
  }

  /* package */ SEConfig getConfig() {
    return this.config;
  }
}
