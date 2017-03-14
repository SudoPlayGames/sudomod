package com.sudoplay.sudoxt.classloader.intercept;

import com.sudoplay.sudoxt.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoxt.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class InterceptClassLoader extends
    ClassLoader implements
    ISandboxClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(InterceptClassLoader.class);

  private Path path;
  private IClassInterceptor classInterceptor;

  public InterceptClassLoader(
      Path path,
      ClassLoader parent,
      IClassInterceptor classInterceptor
  ) {
    super(parent);
    this.path = path;

    SecurityManager security = System.getSecurityManager();

    if (security != null) {
      security.checkCreateClassLoader();
    }

    this.classInterceptor = classInterceptor;
  }

  @Override
  public Path getPath() {
    return this.path;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      // check if we've intercepted the class previously
      Class<?> c = this.findLoadedClass(name);

      if (c == null) {
        ClassLoader parent = this.getParent();

        if (parent != null) {

          try {
            // load the class upstream
            c = parent.loadClass(name);

          } catch (ClassNotFoundException e) {
            //
          }

          // if it is an annotated class, intercept it
          if (c != null && c.getDeclaredAnnotation(InterceptClass.class) != null) {
            c = this.intercept(name);
          }
        }
      }

      if (c == null) {
        throw new ClassNotFoundException(name);
      }

      return c;
    }
  }

  private Class<?> intercept(String name) throws ClassNotFoundException {
    byte[] classBytes = this.getClassBytes(name);
    Class<?> c = super.defineClass(null, classBytes, 0, classBytes.length);

    if (!c.getName().equals(name)) {
      throw new ClassNotFoundException(name);
    }

    final Class<?> cc = c;
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      this.classInterceptor.intercept(cc);
      return null;
    });

    c = cc;

    return c;
  }

  private byte[] getClassBytes(String name) throws ClassNotFoundException {

    String path = name.replace(".", "/").concat(".class");

    InputStream inputStream = AccessController.doPrivileged(
        (PrivilegedAction<InputStream>) () -> InterceptClassLoader.this.getResourceAsStream(path)
    );

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
      CloseUtil.close(inputStream, LOG);
    }

    return byteArrayOutputStream.toByteArray();
  }
}
