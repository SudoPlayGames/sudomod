package com.sudoplay.sudoext.classloader.asm.filter;

import com.sudoplay.sudoext.classloader.filter.IClassFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class AllowedJavaLangClassFilter implements
    IClassFilter {

  private Set<String> allowedSet;

  public AllowedJavaLangClassFilter() {
    this.allowedSet = new HashSet<>();

    this.allowedSet.addAll(Arrays.asList(

        // classes
        Boolean.class.getName(),
        Byte.class.getName(),
        Character.class.getName(),
        Class.class.getName(),
        Double.class.getName(),
        Enum.class.getName(),
        Float.class.getName(),
        Integer.class.getName(),
        Long.class.getName(),
        Math.class.getName(),
        Object.class.getName(),
        Short.class.getName(),
        String.class.getName(),

        // annotation
        Deprecated.class.getName(),
        Override.class.getName(),
        SuppressWarnings.class.getName()
    ));
  }

  @Override
  public boolean isAllowed(String name) {
    return this.allowedSet.contains(name);
  }
}
