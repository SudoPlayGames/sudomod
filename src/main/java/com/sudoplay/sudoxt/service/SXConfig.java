package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.versioning.ArtifactVersion;
import com.sudoplay.sudoxt.versioning.DefaultArtifactVersion;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration context.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class SXConfig {

  /* package */ Path[] locations;
  /* package */ Path tempLocation;

  /* package */ String metaFilename;
  /* package */ String compressedFileExtension;
  /* package */ boolean autoPreloadRegisteredPlugins;

  /* package */ ArtifactVersion apiVersion;

  /* package */ Charset charset;

  /* package */ SXConfig() {

    // init default paths
    this.locations = new Path[0];
    this.tempLocation = Paths.get("plugins-temp");

    // init default strings
    this.metaFilename = "meta.json";
    this.compressedFileExtension = "zip";
    this.autoPreloadRegisteredPlugins = true;

    // init default objects
    this.apiVersion = new DefaultArtifactVersion("0");

    this.charset = Charset.forName("UTF-8");
  }

  /* package */ Path[] getLocations() {
    return this.locations;
  }

  /* package */ String getMetaFilename() {
    return this.metaFilename;
  }

  /* package */ Path getTempLocation() {
    return this.tempLocation;
  }

  /* package */ String getCompressedFileExtension() {
    return this.compressedFileExtension;
  }

  /* package */ ArtifactVersion getApiVersion() {
    return this.apiVersion;
  }

  /* package */ Charset getCharset() {
    return this.charset;
  }

  /* package */ boolean isAutoPreloadRegisteredPlugins() {
    return this.autoPreloadRegisteredPlugins;
  }
}
