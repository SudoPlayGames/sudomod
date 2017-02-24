package com.sudoplay.sudoext.service;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ResourceStringParser {

  public ResourceLocation parse(String resourceString) throws ResourceStringParseException {

    String[] split = resourceString.split(":");

    if (split.length != 2) {
      throw new ResourceStringParseException(String.format("Invalid resource string [%s]", resourceString));
    }

    String id = split[0];
    String resource = split[1];

    return new ResourceLocation(id, resource);
  }

}
