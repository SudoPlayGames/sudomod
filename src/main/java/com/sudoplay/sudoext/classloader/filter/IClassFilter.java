package com.sudoplay.sudoext.classloader.filter;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IClassFilter {

  default boolean isAllowed(String name) {
    return false;
  }

  default boolean isRestricted(String name) {
    return false;
  }
}
