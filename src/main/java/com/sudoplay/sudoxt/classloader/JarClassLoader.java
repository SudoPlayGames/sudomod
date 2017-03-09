package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.classloader.asm.exception.RestrictedUseException;
import com.sudoplay.sudoxt.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoxt.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoxt.util.CloseUtil;
import com.sudoplay.sudoxt.util.InputStreamByteArrayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/22/2017.
 */
/* package */ class JarClassLoader extends
    URLClassLoader implements
    IContainerClassLoader,
    ISandboxClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JarClassLoader.class);

  private final Path path;
  private final IByteCodeTransformer byteCodeTransformer;
  private final InputStreamByteArrayConverter inputStreamByteArrayConverter;

  /* package */ JarClassLoader(
      Path path,
      URL[] urls,
      ClassLoader parent,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter
  ) {
    super(urls, parent);
    this.path = path;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
  }

  @Override
  public Path getPath() {
    return this.path;
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

      if (e instanceof RestrictedUseException) {
        LOG.error(e.getMessage());
      }

      throw new ClassNotFoundException(name, e);

    } finally {
      CloseUtil.close(inputStream, LOG);
    }
  }
}
