package com.sudoplay.sudoext.classloader.asm.filter;

import com.sudoplay.sudoext.classloader.filter.IClassFilter;

import java.util.*;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class AllowedJavaUtilClassFilter implements
    IClassFilter {

  private Set<String> allowedSet;

  public AllowedJavaUtilClassFilter() {
    this.allowedSet = new HashSet<>();

    this.allowedSet.addAll(Arrays.asList(

        // classes
        ArrayList.class.getName(),

        // interfaces
        List.class.getName()
    ));
  }

  @Override
  public boolean isAllowed(String name) {
    return this.allowedSet.contains(name);
  }
}
