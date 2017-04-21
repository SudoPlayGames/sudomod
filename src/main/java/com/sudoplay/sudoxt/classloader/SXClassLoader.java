package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.classloader.asm.exception.RestrictedUseException;
import com.sudoplay.sudoxt.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoxt.container.Container;
import com.sudoplay.sudoxt.util.CloseUtil;
import com.sudoplay.sudoxt.util.InputStreamByteArrayConverter;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.AccessControlException;
import java.util.List;

/**
 * Created by codetaylor on 3/10/2017.
 */
public class SXClassLoader extends
    URLClassLoader implements
    ISandboxClassLoader,
    IContainerClassLoader {

  public static final int JAR = 1;
  public static final int SOURCE = 2;
  public static final int DEPENDENCY = 4;
  public static final int PARENT = 8;

  private static final Logger LOG = LoggerFactory.getLogger(SXClassLoader.class);

  private final Path path;
  private final List<Container> containerList;
  private final IByteCodeTransformer byteCodeTransformer;
  private final InputStreamByteArrayConverter inputStreamByteArrayConverter;
  private final IClassFilterPredicate classFilterPredicate;
  private final ICompiler compiler;
  private final boolean debug;

  public SXClassLoader(
      Path path,
      URL[] jarUrls,
      ClassLoader parent,
      List<Container> containerList,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter,
      IClassFilterPredicate classFilterPredicate,
      ICompiler compiler,
      boolean debug
  ) {
    super(jarUrls, parent);
    this.path = path;
    this.containerList = containerList;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
    this.classFilterPredicate = classFilterPredicate;
    this.compiler = compiler;
    this.debug = debug;
  }

  @Override
  public Path getPath() {
    return this.path;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return this.loadClass(name, (PARENT | JAR | SOURCE | DEPENDENCY));
  }

  @Override
  public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Class<?> loadClass(
      String name,
      int flags
  ) throws ClassNotFoundException {

    synchronized (this.getClassLoadingLock(name)) {
      Class<?> c = this.findLoadedClass(name);

      if (c != null) {
        return c;
      }

      ClassLoader parent = this.getParent();

      try {

        if ((flags & PARENT) == PARENT && parent != null) {
          c = parent.loadClass(name);

          // check class filters
          if (c != null && !this.classFilterPredicate.isAllowed(name)) {
            LOG.error("Class [{}] is not allowed", name);
            throw new AccessControlException(String.format("Class [%s] is not allowed", name));
          }
        }

      } catch (ClassNotFoundException e) {
        //
      }

      if (c != null) {
        return c;
      }

      c = this.findClass(name, flags);

      if (c == null) {
        throw new ClassNotFoundException(name);
      }

      return c;
    }
  }

  private Class<?> findClass(String name, int flags) throws ClassNotFoundException {

    Class<?> c = null;

    // check jars first
    if ((flags & JAR) == JAR) {
      c = this.findJarClass(name);

      if (c != null) {
        return c;
      }
    }

    // check source next
    if ((flags & SOURCE) == SOURCE) {
      c = this.findSourceClass(name);

      if (c != null) {
        return c;
      }
    }

    // check dependencies last
    if ((flags & DEPENDENCY) == DEPENDENCY) {
      c = this.findDependencyClass(name);
    }

    return c;
  }

  @Nullable
  private Class<?> findJarClass(final String name) {

    String path = name.replace('.', '/').concat(".class");
    InputStream inputStream = this.getLocalResourceAsStream(path);

    if (inputStream == null) {
      return null;
    }

    try {
      byte[] bytecode = this.byteCodeTransformer.transform(
          this.inputStreamByteArrayConverter.convert(inputStream)
      );

      return this.defineClass(name, bytecode, 0, bytecode.length);

    } catch (Exception e) {

      if (e instanceof RestrictedUseException) {
        throw (RestrictedUseException) e;
      }

    } finally {
      CloseUtil.close(inputStream, LOG);
    }

    return null;
  }

  @Nullable
  private Class<?> findDependencyClass(String name) {
    Class<?> c = null;

    // invoke dependency class loaders
    for (Container container : this.containerList) {

      try {
        c = container.loadClass(name, (JAR | SOURCE));

      } catch (ClassNotFoundException e) {
        LOG.trace("Class [{}] not found by dependency [{}]", name, container);
      }

      if (c != null) {
        break;
      }
    }

    return c;
  }

  private Class<?> findSourceClass(String name) throws ClassNotFoundException {
    assert name != null;

    byte[] bytecode;

    bytecode = this.compiler.compile(name);

    if (bytecode == null) {
      return null;
    }

    try {
      bytecode = this.byteCodeTransformer.transform(bytecode);
      return this.defineClass(name, bytecode, 0, bytecode.length);

    } catch (Exception e) {

      if (e instanceof RestrictedUseException) {
        LOG.error(e.getMessage());
        throw (RestrictedUseException) e;
      }
    }

    return null;
  }

  /**
   * The same as {@link URLClassLoader#getResourceAsStream(String)} but does not check parent class loaders.
   *
   * @param name name of the resource
   * @return input stream
   */
  private InputStream getLocalResourceAsStream(String name) {
    URL url = this.findResource(name);

    try {
      return url != null ? url.openStream() : null;

    } catch (IOException e) {
      return null;
    }
  }

}
