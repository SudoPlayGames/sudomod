package com.sudoplay.sudoext.classloader.asm.filter;

import com.sudoplay.sudoext.classloader.filter.IClassFilter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class AllowedPrimitivesClassFilter implements
    IClassFilter {

  @Override
  public boolean isAllowed(String name) {

    switch (name) {
      case "Z": // boolean
      case "C": // char
      case "F": // float
      case "D": // double
      case "B": // byte
      case "S": // short
      case "I": // int
      case "J": // long
        return true;
    }
    return false;
  }
}
