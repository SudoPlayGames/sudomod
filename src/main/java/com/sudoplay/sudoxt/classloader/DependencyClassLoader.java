package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoxt.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/22/2017.
 */
/* package */ class DependencyClassLoader extends
    ClassLoader implements
    IContainerClassLoader,
    ISandboxClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(DependencyClassLoader.class);

  private Path path;
  private List<Container> containerList;

  /* package */ DependencyClassLoader(
      Path path,
      ClassLoader parent,
      List<Container> containerList
  ) {
    super(parent);
    this.path = path;

    SecurityManager security = System.getSecurityManager();

    if (security != null) {
      security.checkCreateClassLoader();
    }

    this.containerList = containerList;
  }

  @Override
  public Path getPath() {
    return this.path;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    Class<?> c = null;

    // invoke dependency class loaders
    for (Container container : this.containerList) {

      try {
        c = container.loadClassWithoutDependencyCheck(name);

      } catch (ClassNotFoundException e) {
        LOG.trace("Class [{}] not found by [{}]", name, container);
      }

      if (c != null) {
        break;
      }
    }

    if (c == null) {
      throw new ClassNotFoundException(name);
    }

    return c;
  }

  @Override
  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    synchronized (this.getClassLoadingLock(name)) {

      Class<?> c = null;

      ClassLoader parent = this.getParent();

      try {

        if (parent != null) {

          if (parent instanceof IContainerClassLoader) {
            c = ((IContainerClassLoader) parent).loadClassWithoutDependencyCheck(name);
          }
        }
      } catch (ClassNotFoundException e) {
        LOG.trace("Class [{}] not found by [{}]", name, parent.getClass());
      }

      // skip dependency check

      if (c == null) {
        throw new ClassNotFoundException(name);
      }

      return c;
    }
  }
}