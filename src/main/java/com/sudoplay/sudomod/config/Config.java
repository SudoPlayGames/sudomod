package com.sudoplay.sudomod.config;

import java.io.File;

/**
 * Configuration context.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class Config {

  private File modDataLocation;
  private File modLocation;
  private String modInfoFilename;
  private String defaultApiVersionString;

  /* package */ Config() {
    this.defaultApiVersionString = "[0,)";
  }

  public File getModDataLocation() {
    return modDataLocation;
  }

  /* package */ void setModDataLocation(File modDataLocation) {
    this.modDataLocation = modDataLocation;
  }

  public File getModLocation() {
    return modLocation;
  }

  /* package */ void setModLocation(File modLocation) {
    this.modLocation = modLocation;
  }

  public String getModInfoFilename() {
    return modInfoFilename;
  }

  /* package */ void setModInfoFilename(String modInfoFilename) {
    this.modInfoFilename = modInfoFilename;
  }

  public String getDefaultApiVersionString() {
    return defaultApiVersionString;
  }

  /* package */ void setDefaultApiVersionString(String defaultApiVersionString) {
    this.defaultApiVersionString = defaultApiVersionString;
  }
}
