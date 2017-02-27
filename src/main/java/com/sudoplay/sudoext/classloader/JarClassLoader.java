package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.security.ISecureClassLoader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by codetaylor on 2/22/2017.
 */
/* package */ class JarClassLoader extends
    URLClassLoader implements
    IClassLoader,
    ISecureClassLoader {

  /* package */ JarClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  @Override
  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = findLoadedClass(name);

      // skip parent check

      if (c == null) {
        c = findClass(name);
      }

      return c;
    }
  }
}
