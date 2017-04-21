package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.classloader.asm.callback.ICallbackDelegate;
import com.sudoplay.sudoxt.classloader.asm.callback.InjectedCallback;
import com.sudoplay.sudoxt.classloader.asm.callback.NoOpCallbackDelegate;
import com.sudoplay.sudoxt.container.Container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class SXPluginReference<P> {

  private String report;
  private ICallbackDelegate callbackDelegate;

  public interface InvokeVoidHandler<P> {
    void invoke(P plugin);
  }

  public interface InvokeReturnHandler<P, R> {
    R invoke(P plugin);
  }

  private Class<P> pClass;
  private String resourceString;
  private Container container;

  /* package */ SXPluginReference(
      Class<P> pClass,
      String resourceString,
      Container container
  ) {
    this.pClass = pClass;
    this.resourceString = resourceString;
    this.container = container;
  }

  /* package */ void preLoad() throws SXPluginException {

    try {
      this.get();

    } catch (Exception e) {
      throw this.getException(e);
    }
  }

  public void invoke(InvokeVoidHandler<P> handler) throws SXPluginException {

    try {

      if (InjectedCallback.DELEGATE == NoOpCallbackDelegate.INSTANCE) {
        this.callbackDelegate = this.container.getCallbackDelegate();
        InjectedCallback.DELEGATE = this.callbackDelegate;
      }

      handler.invoke(this.get());

    } catch (Exception e) {
      throw this.getException(e);

    } finally {

      if (this.callbackDelegate == InjectedCallback.DELEGATE) {
        this.report = this.callbackDelegate.getReport();
        this.callbackDelegate = null;
        InjectedCallback.DELEGATE = NoOpCallbackDelegate.INSTANCE;
      }
    }
  }

  private SXPluginException getException(Exception e) {
    return new SXPluginException(
        String.format(
            "[%s]: %s",
            e.getClass().getSimpleName(),
            e.getMessage()
        ),
        e
    );
  }

  public <R> R invoke(Class<R> rClass, InvokeReturnHandler<P, R> handler) throws SXPluginException {

    try {
      if (InjectedCallback.DELEGATE == null) {
        this.callbackDelegate = this.container.getCallbackDelegate();
        InjectedCallback.DELEGATE = this.callbackDelegate;
      }

      return handler.invoke(this.get());

    } catch (Exception e) {
      throw this.getException(e);

    } finally {

      if (this.callbackDelegate == InjectedCallback.DELEGATE) {
        this.report = this.callbackDelegate.getReport();
        this.callbackDelegate = null;
        InjectedCallback.DELEGATE = null;
      }
    }
  }

  public String getReport() {
    return this.report;
  }

  private P get() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
    return this.container.getPlugin(this.resourceString, this.pClass);
  }
}
