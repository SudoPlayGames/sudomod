package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.security.ISandboxClassLoader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by codetaylor on 2/22/2017.
 */
/* package */ class JarClassLoader extends
    URLClassLoader implements
    IClassLoader,
    ISandboxClassLoader {

  private final IByteCodeTransformer byteCodeTransformer;

  /* package */ JarClassLoader(
      URL[] urls,
      ClassLoader parent,
      IByteCodeTransformer byteCodeTransformer
  ) {
    super(urls, parent);
    this.byteCodeTransformer = byteCodeTransformer;
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

  protected Class<?> findClass(final String name)
      throws ClassNotFoundException {

    String path = name.replace('.', '/').concat(".class");
    InputStream inputStream = this.getResourceAsStream(path);

    if (inputStream == null) {
      throw new ClassNotFoundException(name);
    }

    try {
      byte[] bytecode = this.byteCodeTransformer.transform(inputStream);
      return defineClass(name, bytecode, 0, bytecode.length);

    } catch (Exception e) {
      throw new ClassNotFoundException(name, e);
    }
  }
}
