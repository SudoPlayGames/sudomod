package com.sudoplay.sudomod.mod.classloader;

import com.sudoplay.sudomod.mod.container.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class ModDependencyClassLoader extends
    ClassLoader implements
    IModClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ModDependencyClassLoader.class);
  private List<ModContainer> modContainerList;

  public ModDependencyClassLoader(ClassLoader parent, List<ModContainer> modContainerList) {
    super(parent);
    this.modContainerList = modContainerList;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return this.loadClass(name, false);
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
            c = parent.loadClass(name);
          }

        } catch (ClassNotFoundException e) {
          // swallow
          LOG.trace("Class [{}] not found by [{}]", name, parent.getClass());
        }

        if (c == null) {

          // invoke dependency class loaders
          for (ModContainer modContainer : this.modContainerList) {
            IModClassLoader modClassLoader = modContainer.getModClassLoader();

            try {
              c = modClassLoader.loadClass(name);

            } catch (ClassNotFoundException e) {
              // swallow
              LOG.trace("Class [{}] not found by [{}]", name, modClassLoader.getClass());
            }

            if (c != null) {
              break;
            }
          }
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

  @Override
  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      // First, check if the class has already been loaded
      Class<?> c = findLoadedClass(name);

      if (c == null) {
        ClassLoader parent = this.getParent();

        try {

          if (parent != null) {

            if (parent instanceof IModClassLoader) {
              c = ((IModClassLoader) parent).loadClassWithoutDependencyCheck(name);

            } else {
              c = parent.loadClass(name);
            }
          }
        } catch (ClassNotFoundException e) {
          // swallow
          LOG.debug("Class [{}] not found by [{}]", name, parent.getClass());
        }
      }

      // skip dependency check

      return c;
    }
  }
}
