package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.container.Container;

/**
 * Created by codetaylor on 2/23/2017.
 */
public interface IClassInterceptProcessor {

  void process(Class<?> interceptedClass, Container container);
}
