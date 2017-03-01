package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;

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
  private final InputStreamByteArrayConverter inputStreamByteArrayConverter;

  /* package */ JarClassLoader(
      URL[] urls,
      ClassLoader parent,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter
  ) {
    super(urls, parent);
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
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
      byte[] bytecode = this.byteCodeTransformer.transform(
          this.inputStreamByteArrayConverter.convert(inputStream)
      );

      return defineClass(name, bytecode, 0, bytecode.length);

    } catch (Exception e) {
      throw new ClassNotFoundException(name, e);
    }
  }
}
