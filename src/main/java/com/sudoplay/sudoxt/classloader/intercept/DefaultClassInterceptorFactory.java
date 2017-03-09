package com.sudoplay.sudoxt.classloader.intercept;

import com.sudoplay.sudoxt.container.Container;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class DefaultClassInterceptorFactory implements
    IClassInterceptorFactory {

  private StaticInjector<?>[] staticInjectors;

  public DefaultClassInterceptorFactory(StaticInjector<?>[] staticInjectors) {
    this.staticInjectors = staticInjectors;
  }

  @Override
  public IClassInterceptor create(Container container) {
    return new DefaultClassInterceptor(container, this.staticInjectors);
  }
}
