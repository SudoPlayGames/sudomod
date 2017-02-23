package com.sudoplay.sudomod;

import com.sudoplay.sudomod.mod.container.ModContainer;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class PluginWrapper<P> {

  private Class<P> pClass;
  private String resourceString;
  private ModContainer modContainer;

  public PluginWrapper(Class<P> pClass, String resourceString, ModContainer modContainer) {
    this.pClass = pClass;
    this.resourceString = resourceString;
    this.modContainer = modContainer;
  }

  public P get() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
    return this.modContainer.get(this.resourceString, this.pClass);
  }
}
