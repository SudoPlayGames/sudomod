package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.filter.IClassFilterPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.AccessControlException;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class FilteredClassLoader extends
    ClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(FilteredClassLoader.class);

  private IClassFilterPredicate classFilterPredicate;

  public FilteredClassLoader(ClassLoader parent, IClassFilterPredicate classFilterPredicate) {
    super(parent);

    SecurityManager security = System.getSecurityManager();

    if (security != null) {
      security.checkCreateClassLoader();
    }

    this.classFilterPredicate = classFilterPredicate;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = null;

      ClassLoader parent = this.getParent();

      try {

        if (parent != null) {
          c = parent.loadClass(name);

          if (c != null && !this.classFilterPredicate.isAllowed(name)) {
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

      return c;
    }
  }
}
