package com.sudoplay.sudoext.classloader;

import org.codehaus.janino.JavaSourceClassLoader;
import org.codehaus.janino.JavaSourceIClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class SourceClassLoader extends
    JavaSourceClassLoader implements
    IClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(SourceClassLoader.class);

  public SourceClassLoader(ClassLoader parentClassLoader, JavaSourceIClassLoader iClassLoader) {
    super(parentClassLoader, iClassLoader);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return this.loadClass(name, false);
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

            if (parent instanceof IClassLoader) {
              c = ((IClassLoader) parent).loadClassWithoutDependencyCheck(name);

            } else {
              c = parent.loadClass(name);
            }
          }
        } catch (ClassNotFoundException e) {
          LOG.trace("Class [{}] not found by [{}]", name, parent.getClass());
        }

        if (c == null) {
          // If still not found, then invoke findClass in order
          // to find the class.
          c = findClass(name);
        }
      }

      return c;
    }
  }
}