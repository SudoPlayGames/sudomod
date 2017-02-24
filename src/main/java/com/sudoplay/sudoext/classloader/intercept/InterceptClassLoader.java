package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.classloader.IClassLoader;
import com.sudoplay.sudoext.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class InterceptClassLoader extends
    ClassLoader implements
    IClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(InterceptClassLoader.class);

  private IClassInterceptor classInterceptor;

  public InterceptClassLoader(ClassLoader parent, IClassInterceptor classInterceptor) {
    super(parent);
    this.classInterceptor = classInterceptor;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = this.findLoadedClass(name);

      if (c == null && this.classInterceptor.canIntercept(name)) {
        c = this.interceptClass(name);

        if (c != null) {
          return c;
        }
      }

      return super.loadClass(name, resolve);
    }
  }

  @Override
  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = this.findLoadedClass(name);

      if (c == null && this.classInterceptor.canIntercept(name)) {
        c = this.interceptClass(name);
      }

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
          throw new ClassNotFoundException(name);
        }
      }

      return c;
    }
  }

  private Class<?> interceptClass(String name) throws ClassNotFoundException {

    String path = name.replace(".", "/").concat(".class");
    InputStream inputStream = this.getResourceAsStream(path);

    if (inputStream == null) {
      throw new ClassNotFoundException(name);
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    try {
      byte[] buffer = new byte[4096];

      for (; ; ) {
        int bytesRead = inputStream.read(buffer);

        if (bytesRead == -1) {
          break;
        }

        byteArrayOutputStream.write(buffer, 0, bytesRead);
      }

    } catch (IOException e) {
      throw new ClassNotFoundException(String.format("Reading class file from [%s]", path), e);

    } finally {
      FileUtils.close(inputStream);
    }

    byte[] classBytes = byteArrayOutputStream.toByteArray();

    Class<?> aClass = super.defineClass(null, classBytes, 0, classBytes.length);

    if (!aClass.getName().equals(name)) {
      throw new ClassNotFoundException(name);
    }

    this.classInterceptor.intercept(aClass);

    return aClass;
  }
}
