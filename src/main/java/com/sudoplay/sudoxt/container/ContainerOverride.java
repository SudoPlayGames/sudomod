package com.sudoplay.sudoxt.container;

/**
 * Created by codetaylor on 3/11/2017.
 */
public class ContainerOverride {

  private String resource;
  private Container container;

  public ContainerOverride(String resource, Container container) {
    this.resource = resource;
    this.container = container;
  }

  public String getResource() {
    return this.resource;
  }

  public Container getContainer() {
    return this.container;
  }
}
