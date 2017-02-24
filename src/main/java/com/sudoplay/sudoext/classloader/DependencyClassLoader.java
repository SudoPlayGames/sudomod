package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class DependencyClassLoader extends
    ClassLoader implements
    IClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(DependencyClassLoader.class);
  private List<Container> containerList;

  public DependencyClassLoader(ClassLoader parent, List<Container> containerList) {
    super(parent);
    this.containerList = containerList;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = null;

      ClassLoader parent = this.getParent();

      try {

        if (parent != null) {
          c = parent.loadClass(name);
        }

      } catch (ClassNotFoundException e) {
        LOG.trace("Class [{}] not found by [{}]", name, parent.getClass());
      }

      if (c == null) {

        // invoke dependency class loaders
        for (Container container : this.containerList) {
          IClassLoader classLoader = container.getClassLoader();

          try {
            c = classLoader.loadClassWithoutDependencyCheck(name);

          } catch (ClassNotFoundException e) {
            LOG.trace("Class [{}] not found by [{}]", name, classLoader.getClass());
          }

          if (c != null) {
            break;
          }
        }
      }

      if (c == null) {
        throw new ClassNotFoundException(name);
      }

      return c;
    }
  }

  @Override
  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = null;

      ClassLoader parent = this.getParent();

      try {

        if (parent != null) {

          if (parent instanceof IClassLoader) {
            c = ((IClassLoader) parent).loadClassWithoutDependencyCheck(name);

          } else {
            c = parent.loadClass(name);
          }
        }
      } catch (ClassNotFoundException e) {
        LOG.trace("Class [{}] not found by [{}]", name, parent.getClass());
      }

      // skip dependency check

      return c;
    }
  }
}
