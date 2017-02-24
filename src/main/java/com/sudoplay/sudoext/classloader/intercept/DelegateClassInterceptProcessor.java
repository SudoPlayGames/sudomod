package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.container.Container;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class DelegateClassInterceptProcessor implements
    IClassInterceptProcessor {

  private IClassInterceptProcessor[] classInterceptProcessors;

  public DelegateClassInterceptProcessor(IClassInterceptProcessor[] classInterceptProcessors) {
    this.classInterceptProcessors = classInterceptProcessors;
  }

  @Override
  public void process(Class<?> interceptedClass, Container container) {

    for (IClassInterceptProcessor processor : this.classInterceptProcessors) {
      processor.process(interceptedClass, container);
    }
  }
}
