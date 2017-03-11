package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.util.PreCondition;
import org.jetbrains.annotations.NotNull;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class SXResourceLocation {

  private String id;
  private String resource;

  public SXResourceLocation(String resourceString) throws SXResourceStringParseException {
    String[] split = this.parse(resourceString);
    this.id = split[0];
    this.resource = split[1];
  }

  public SXResourceLocation(String id, String resource) {
    this.id = PreCondition.notNull(id);
    this.resource = PreCondition.notNull(resource);
  }

  public String getId() {
    return this.id;
  }

  public String getResource() {
    return this.resource;
  }

  @NotNull
  private String[] parse(String resourceString) throws SXResourceStringParseException {
    String[] split = resourceString.split(":");

    if (split.length != 2) {
      throw new SXResourceStringParseException(String.format("Invalid resource string [%s]", resourceString));
    }
    return split;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SXResourceLocation that = (SXResourceLocation) o;

    if (!id.equals(that.id)) return false;
    return resource.equals(that.resource);

  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + resource.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return this.id + ":" + this.resource;
  }
}
