package com.sudoplay.sudoext.service;

import org.jetbrains.annotations.NotNull;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ResourceLocation {

  private String id;
  private String resource;

  public ResourceLocation(String resourceString) throws ResourceStringParseException {
    String[] split = this.parse(resourceString);
    this.id = split[0];
    this.resource = split[1];
  }

  public ResourceLocation(String id, String resource) {
    this.id = id;
    this.resource = resource;
  }

  public String getId() {
    return this.id;
  }

  public String getResource() {
    return this.resource;
  }

  @NotNull
  private String[] parse(String resourceString) throws ResourceStringParseException {
    String[] split = resourceString.split(":");

    if (split.length != 2) {
      throw new ResourceStringParseException(String.format("Invalid resource string [%s]", resourceString));
    }
    return split;
  }
}
