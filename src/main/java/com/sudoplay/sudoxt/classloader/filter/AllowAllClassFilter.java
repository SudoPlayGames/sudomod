package com.sudoplay.sudoxt.classloader.filter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class AllowAllClassFilter implements
    IClassFilter {

  @Override
  public boolean isAllowed(String name) {
    return true;
  }
}
