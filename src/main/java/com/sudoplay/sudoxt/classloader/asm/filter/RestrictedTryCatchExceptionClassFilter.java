package com.sudoplay.sudoxt.classloader.asm.filter;

import com.sudoplay.sudoxt.classloader.filter.IClassFilter;

/**
 * Restricts base exceptions. This filter is intended to be used to restrict the exceptions that can be used in the
 * try/catch blocks. By restricting these exceptions, we prevent malicious code from intercepting specific runtime
 * exceptions that are thrown by the injected callbacks designed to terminate the execution of third-party code.
 * <p>
 * Created by codetaylor on 3/1/2017.
 */
public class RestrictedTryCatchExceptionClassFilter implements
    IClassFilter {

  @Override
  public boolean isAllowed(String name) {
    // allow all exceptions
    return true;
  }

  @Override
  public boolean isRestricted(String name) {

    // restrict the following exceptions
    switch (name) {
      case "java.lang.Throwable":
      case "java.lang.Exception":
      case "java.lang.RuntimeException":
        return true;
    }
    return false;
  }
}
