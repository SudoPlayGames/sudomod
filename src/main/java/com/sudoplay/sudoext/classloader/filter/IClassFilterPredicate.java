package com.sudoplay.sudoext.classloader.filter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IClassFilterPredicate {
  boolean isAllowed(String name);
}
