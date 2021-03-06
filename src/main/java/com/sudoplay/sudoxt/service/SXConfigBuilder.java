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
public class SXConfigBuilder {

  private SXConfig config;

  public SXConfigBuilder() {
    this.config = new SXConfig();
  }

  public SXConfigBuilder setLocations(@NotNull Path[] locations) {
    this.config.locations = PreCondition.notNull(locations);
    return this;
  }

  public SXConfigBuilder setMetaFilename(@NotNull String filename) {
    this.config.metaFilename = PreCondition.notNull(filename);
    return this;
  }

  public SXConfigBuilder setTempLocation(@NotNull Path tempLocation) {
    this.config.tempLocation = PreCondition.notNull(tempLocation);
    return this;
  }

  public SXConfigBuilder setCompressedFileExtension(@NotNull String compressedFileExtension) {
    this.config.compressedFileExtension = PreCondition.notNull(compressedFileExtension);
    return this;
  }

  public SXConfigBuilder setApiVersion(@NotNull String apiVersionString) {
    this.config.apiVersion = new DefaultArtifactVersion(PreCondition.notNull(apiVersionString));
    return this;
  }

  public SXConfigBuilder setCharset(@NotNull Charset charset) {
    this.config.charset = PreCondition.notNull(charset);
    return this;
  }

  public SXConfigBuilder setAutoPreloadRegisteredPlugins(boolean autoPreload) {
    this.config.autoPreloadRegisteredPlugins = autoPreload;
    return this;
  }

  @NotNull/* package */ SXConfig getConfig() {
    return this.config;
  }
}
