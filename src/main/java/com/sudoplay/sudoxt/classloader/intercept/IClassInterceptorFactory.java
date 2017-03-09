package com.sudoplay.sudoxt.classloader.intercept;

import com.sudoplay.sudoxt.container.Container;

/**
 * Created by codetaylor on 2/23/2017.
 */
public interface IClassInterceptorFactory {

  IClassInterceptor create(Container container);
}
