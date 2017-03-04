package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoext.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class InterceptClassLoader extends
    ClassLoader implements
    ISandboxClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(InterceptClassLoader.class);

  private IClassInterceptor classInterceptor;

  public InterceptClassLoader(
      ClassLoader parent,
      IClassInterceptor classInterceptor
  ) {
    super(parent);

    SecurityManager security = System.getSecurityManager();

    if (security != null) {
      security.checkCreateClassLoader();
    }

    this.classInterceptor = classInterceptor;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = this.findLoadedClass(name);

      if (c == null && this.classInterceptor.canIntercept(name)) {
        byte[] classBytes = this.getClassBytes(name);
        c = super.defineClass(null, classBytes, 0, classBytes.length);

        if (!c.getName().equals(name)) {
          throw new ClassNotFoundException(name);
        }

        final Class<?> cc = c;
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
          // this is privileged because the static callbacks injected via asm are
          // loaded within the context of the sandbox and consequently fail
          // interception due to the reflection involved
          this.classInterceptor.intercept(cc);
          return null;
        });
        c = cc;
      }

      if (c != null) {
        return c;
      }

      return super.loadClass(name, resolve);
    }
  }

  private byte[] getClassBytes(String name) throws ClassNotFoundException {

    String path = name.replace(".", "/").concat(".class");

    InputStream inputStream = AccessController.doPrivileged((PrivilegedAction<InputStream>) () -> {
      // this is privileged because the static callbacks injected via asm are
      // loaded within the context of the sandbox and consequently fail
      // to read the filesystem
      return InterceptClassLoader.this.getResourceAsStream(path);
    });

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
