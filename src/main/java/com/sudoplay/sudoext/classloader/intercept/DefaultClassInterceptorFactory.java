package com.sudoplay.sudoext.classloader.intercept;

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
  public IClassInterceptor create(String id) {
    return new DefaultClassInterceptor(id, this.classIntercepts);
  }
}
