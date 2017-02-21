package com.sudoplay.sudomod.mod;

import org.codehaus.janino.JavaSourceClassLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModClassLoader extends
    URLClassLoader {

  private JavaSourceClassLoader javaSourceClassLoader;
  private List<ModClassLoader> dependencyClassLoaderList;

  public ModClassLoader(
      URL[] urls,
      JavaSourceClassLoader javaSourceClassLoader,
      ClassLoader parent
  ) {
    super(urls, parent);
    this.javaSourceClassLoader = javaSourceClassLoader;
    this.dependencyClassLoaderList = Collections.emptyList();
  }

  public void setDependencyClassLoaderList(List<ModClassLoader> dependencyClassLoaderList) {
    this.dependencyClassLoaderList = dependencyClassLoaderList;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return this.loadClass(name, false);
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    Class<?> c;

    // first, check locally
    c = loadLocalClass(name);

    // next, check dependencies
    if (c == null) {

      for (ModClassLoader modClassLoader : this.dependencyClassLoaderList) {

        // call loadLocalClass instead of loadClass so we don't follow the dependency chain
        if ((c = modClassLoader.loadLocalClass(name)) != null) {
          break;
        }
      }
    }

    // finally, check parent
    if (c == null && getParent() != null) {
      c = getParent().loadClass(name);
    }

    if (resolve) {
      resolveClass(c);
    }

    return c;
  }

  private Class<?> loadLocalClass(String name) {

    Class<?> c = null;

    // first check jars
    try {
      synchronized (getClassLoadingLock(name)) {
        // First, check if the class has already been loaded
        c = findLoadedClass(name);

        if (c == null) {
          c = findClass(name);
        }
      }

    } catch (ClassNotFoundException e) {
      // swallow
    }

    // next check source
    if (c == null) {

      try {
        c = this.javaSourceClassLoader.loadClass(name);

      } catch (ClassNotFoundException e) {
        // swallow
      }
    }
    return c;
  }
}
