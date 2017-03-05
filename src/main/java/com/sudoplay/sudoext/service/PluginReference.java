package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.classloader.asm.callback.InjectedCallback;
import com.sudoplay.sudoext.container.Container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class PluginReference<P extends Plugin> {

  public interface InvokeVoidHandler<P> {
    void invoke(P plugin);
  }

  public interface InvokeReturnHandler<P, R> {
    R invoke(P plugin);
  }

  private Class<P> pClass;
  private String resourceString;
  private Container container;

  /* package */ PluginReference(Class<P> pClass, String resourceString, Container container) {
    this.pClass = pClass;
    this.resourceString = resourceString;
    this.container = container;
  }

  public void invokeVoid(InvokeVoidHandler<P> handler) throws IllegalAccessException,
      ClassNotFoundException, InstantiationException {
    handler.invoke(this.get());
  }

  public <R> R invokeReturn(InvokeReturnHandler<P, R> handler) throws IllegalAccessException,
      ClassNotFoundException, InstantiationException {
    return handler.invoke(this.get());
  }

  private P get() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
    InjectedCallback.DELEGATE.reset();
    return this.container.getPlugin(this.resourceString, this.pClass);
  }
}
