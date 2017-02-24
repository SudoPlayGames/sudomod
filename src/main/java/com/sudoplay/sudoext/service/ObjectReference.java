package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.container.Plugin;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class ObjectReference<P extends Plugin> {

  private Class<P> pClass;
  private String resourceString;
  private Container container;

  public ObjectReference(Class<P> pClass, String resourceString, Container container) {
    this.pClass = pClass;
    this.resourceString = resourceString;
    this.container = container;
  }

  public P get() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
    return this.container.get(this.resourceString, this.pClass);
  }
}
