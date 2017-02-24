package com.sudoplay.sudoext.service;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ResourceLocation {

  private String id;
  private String resourceString;

  public ResourceLocation(String id, String resourceString) {
    this.id = id;
    this.resourceString = resourceString;
  }

  public String getId() {
    return this.id;
  }

  public String getResourceString() {
    return this.resourceString;
  }
}
