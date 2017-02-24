package com.sudoplay.sudoext.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class JarClassLoader extends
    URLClassLoader implements
    IClassLoader {

  public JarClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
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

      // skip parent check

      if (c == null) {
        c = findClass(name);
      }

      return c;
    }
  }
}