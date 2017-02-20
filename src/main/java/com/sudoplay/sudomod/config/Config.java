package com.sudoplay.sudomod.config;

import com.sudoplay.sudomod.versioning.ArtifactVersion;

import java.nio.file.Path;

/**
 * Configuration context.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class Config {

  /* package */ Path modDataLocation;
  /* package */ Path modLocation;
  /* package */ String modInfoFilename;
  /* package */ String defaultModInfoApiVersionString;
  /* package */ boolean followLinks;
  /* package */ Path modTempLocation;
  /* package */ String compressedModFileExtension;
  /* package */ ArtifactVersion apiVersion;

  /* package */ Config() {
    this.defaultModInfoApiVersionString = "[0,)";
  }

  public Path getModDataLocation() {
    return this.modDataLocation;
  }

  public Path getModLocation() {
    return this.modLocation;
  }

  public String getModInfoFilename() {
    return this.modInfoFilename;
  }

  public String getDefaultModInfoApiVersionString() {
    return this.defaultModInfoApiVersionString;
  }

  public boolean isFollowLinks() {
    return this.followLinks;
  }

  public Path getModTempLocation() {
    return this.modTempLocation;
  }

  public String getCompressedModFileExtension() {
    return this.compressedModFileExtension;
  }

  public ArtifactVersion getApiVersion() {
    return this.apiVersion;
  }
}
