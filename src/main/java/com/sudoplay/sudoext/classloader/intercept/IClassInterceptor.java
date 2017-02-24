package com.sudoplay.sudoext.classloader.intercept;

/**
 * Created by codetaylor on 2/23/2017.
 */
public interface IClassInterceptor {

  boolean canIntercept(String name);

  void intercept(Class<?> aClass);
}
