package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.versioning.ArtifactVersion;
import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration context.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class SEConfig {

  /* package */ Path dataLocation;
  /* package */ Path location;
  /* package */ Path tempLocation;

  /* package */ String metaFilename;
  /* package */ String compressedFileExtension;

  /* package */ ArtifactVersion apiVersion;

  /* package */ SEConfig() {

    // init default paths
    this.dataLocation = Paths.get("plugins-data");
    this.location = Paths.get("plugins");
    this.tempLocation = Paths.get("plugins-temp");

    // init default strings
    this.metaFilename = "meta.json";
    this.compressedFileExtension = ".zip";

    // init default objects
    this.apiVersion = new DefaultArtifactVersion("0");
  }

  /* package */ Path getDataLocation() {
    return this.dataLocation;
  }

  /* package */ Path getLocation() {
    return this.location;
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

}
