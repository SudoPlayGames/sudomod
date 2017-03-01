package com.sudoplay.sudoext.classloader;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IClassFilter {

  boolean isAllowed(String name);
}
