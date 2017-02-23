package com.sudoplay.sudomod.mod.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class FilteredClassLoader extends
    ClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(FilteredClassLoader.class);

  private IClassFilter[] classFilters;

  public FilteredClassLoader(ClassLoader parent, IClassFilter[] classFilters) {
    super(parent);
    this.classFilters = classFilters;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {
      // First, check if the class has already been loaded
      Class<?> c = findLoadedClass(name);

      if (c == null) {
        ClassLoader parent = this.getParent();

        try {

          if (parent != null) {

            if (this.checkFilters(name)) {
              c = parent.loadClass(name);

            } else {
              LOG.trace("Class [{}] denied by [{}]", name, this.getClass());
            }
          }

        } catch (ClassNotFoundException e) {
          LOG.trace("Class [{}] not found by [{}]", name, parent);
        }

        if (c == null) {
          throw new ClassNotFoundException(name);
        }
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
