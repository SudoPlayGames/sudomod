package com.sudoplay.sudoext.classloader.intercept;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class ClassIntercept {

  private Class<?> interceptClass;
  private IClassInterceptProcessor classInterceptProcessor;

  public ClassIntercept(Class<?> interceptClass, IClassInterceptProcessor classInterceptProcessor) {
    this.interceptClass = interceptClass;
    this.classInterceptProcessor = classInterceptProcessor;
  }

  public Class<?> getInterceptClass() {
    return this.interceptClass;
  }

  public IClassInterceptProcessor getClassInterceptProcessor() {
    return this.classInterceptProcessor;
  }
}
