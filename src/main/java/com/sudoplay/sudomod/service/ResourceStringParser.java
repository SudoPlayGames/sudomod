package com.sudoplay.sudomod.service;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ResourceStringParser {

  public ResourceLocation parse(String resourceString) throws ResourceStringParseException {

    String[] split = resourceString.split(":");

    if (split.length != 2) {
      throw new ResourceStringParseException(String.format("Invalid resource string [%s]", resourceString));
    }

    String modId = split[0];
    String resource = split[1];

    return new ResourceLocation(modId, resource);
  }

}
