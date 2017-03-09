package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.util.PreCondition;
import com.sudoplay.sudoxt.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.NotNull;

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

  public SEConfigBuilder setDataLocation(@NotNull Path dataLocation) {
    this.config.dataLocation = PreCondition.notNull(dataLocation);
    return this;
  }

  public SEConfigBuilder setLocation(@NotNull Path location) {
    this.config.location = PreCondition.notNull(location);
    return this;
  }

  public SEConfigBuilder setMetaFilename(@NotNull String filename) {
    this.config.metaFilename = PreCondition.notNull(filename);
    return this;
  }

  public SEConfigBuilder setTempLocation(@NotNull Path tempLocation) {
    this.config.tempLocation = PreCondition.notNull(tempLocation);
    return this;
  }

  public SEConfigBuilder setCompressedFileExtension(@NotNull String compressedFileExtension) {
    this.config.compressedFileExtension = PreCondition.notNull(compressedFileExtension);
    return this;
  }

  public SEConfigBuilder setApiVersion(@NotNull String apiVersionString) {
    this.config.apiVersion = new DefaultArtifactVersion(PreCondition.notNull(apiVersionString));
    return this;
  }

  public SEConfigBuilder setCharset(@NotNull Charset charset) {
    this.config.charset = PreCondition.notNull(charset);
    return this;
  }

  @NotNull
  /* package */ SEConfig getConfig() {
    return this.config;
  }
}
