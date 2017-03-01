package com.sudoplay.sudoext.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.AccessControlException;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class FilteredClassLoader extends
    ClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(FilteredClassLoader.class);

  private IClassFilter[] classFilters;

  public FilteredClassLoader(ClassLoader parent, IClassFilter[] classFilters) {
    super(parent);

    SecurityManager security = System.getSecurityManager();

    if (security != null) {
      security.checkCreateClassLoader();
    }

    this.classFilters = classFilters;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = null;

      ClassLoader parent = this.getParent();

      try {

        if (parent != null) {
          c = parent.loadClass(name);

          if (c != null && !this.checkFilters(name)) {
            LOG.error("Class [{}] is not allowed", name);
            throw new AccessControlException(String.format("Class [%s] is not allowed", name));
          }
        }

      } catch (ClassNotFoundException e) {
        LOG.trace("Class [{}] not found by [{}]", name, parent);
      }

      if (c == null) {
        throw new ClassNotFoundException(name);
      }

      if (resolve) {
        resolveClass(c);
      }

      return c;
    }
  }

  private boolean checkFilters(String name) {

    for (IClassFilter classFilter : this.classFilters) {

      // if any white-list contains the class, let it pass
      if (classFilter.isAllowed(name)) {
        return true;
      }
    }

    return false;
  }
}
