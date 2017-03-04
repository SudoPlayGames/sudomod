package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.container.Container;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class DefaultClassInterceptorFactory implements
    IClassInterceptorFactory {

  private ClassIntercept[] classIntercepts;

  public DefaultClassInterceptorFactory(ClassIntercept[] classIntercepts) {
    this.classIntercepts = classIntercepts;
  }

  @Override
  public IClassInterceptor create(Container container) {
    return new DefaultClassInterceptor(container, this.classIntercepts);
  }
}
