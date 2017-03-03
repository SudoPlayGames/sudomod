package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.container.Container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class PluginReference<P extends Plugin> {

  private Class<P> pClass;
  private String resourceString;
  private Container container;

  /* package */ PluginReference(Class<P> pClass, String resourceString, Container container) {
    this.pClass = pClass;
    this.resourceString = resourceString;
    this.container = container;
  }

  public P get() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
    this.container.getCallbackDelegate().reset();
    return this.container.get(this.resourceString, this.pClass);
  }
}
