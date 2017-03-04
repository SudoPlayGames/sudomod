package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by codetaylor on 2/22/2017.
 */
/* package */ class JarClassLoader extends
    URLClassLoader implements
    IContainerClassLoader,
    ISandboxClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JarClassLoader.class);

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

      if (c == null) {
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

        if (c == null) {
          c = findClass(name);
        }
      }

      if (c == null) {
        throw new ClassNotFoundException(name);
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
