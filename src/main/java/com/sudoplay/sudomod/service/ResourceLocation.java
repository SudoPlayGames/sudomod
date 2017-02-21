package com.sudoplay.sudomod.service;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ResourceLocation {

  private String modId;
  private String resourceString;

  public ResourceLocation(String modId, String resourceString) {
    this.modId = modId;
    this.resourceString = resourceString;
  }

  public String getModId() {
    return this.modId;
  }

  public String getResourceString() {
    return this.resourceString;
  }
}
