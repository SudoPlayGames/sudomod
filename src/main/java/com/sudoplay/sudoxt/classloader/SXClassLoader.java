package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.classloader.asm.exception.RestrictedUseException;
import com.sudoplay.sudoxt.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.security.ISandboxClassLoader;
import com.sudoplay.sudoxt.container.Container;
import com.sudoplay.sudoxt.util.CloseUtil;
import com.sudoplay.sudoxt.util.InputStreamByteArrayConverter;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.Descriptor;
import org.codehaus.janino.UnitCompiler;
import org.codehaus.janino.util.ClassFile;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.AccessControlException;
import java.util.*;

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

  private static final Logger LOG = LoggerFactory.getLogger(SXClassLoader.class);

  private final Path path;
  private final List<Container> containerList;
  private final IByteCodeTransformer byteCodeTransformer;
  private final InputStreamByteArrayConverter inputStreamByteArrayConverter;
  private SXJavaSourceIClassLoader javaSourceIClassLoader;
  private final IClassFilterPredicate classFilterPredicate;
  private final boolean debug;

  private final Map<String, byte[]> preCompiledClasses;

  public SXClassLoader(
      Path path,
      URL[] jarUrls,
      ClassLoader parent,
      List<Container> containerList,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter,
      IClassFilterPredicate classFilterPredicate,
      boolean debug
  ) {
    super(jarUrls, parent);
    this.path = path;
    this.containerList = containerList;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
    this.classFilterPredicate = classFilterPredicate;
    this.debug = debug;

    this.preCompiledClasses = new HashMap<>();
  }

  public void setJavaSourceIClassLoader(SXJavaSourceIClassLoader iClassLoader) {
    this.javaSourceIClassLoader = iClassLoader;
  }

  @Override
  public Path getPath() {
    return this.path;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return this.loadClass(name, (JAR | SOURCE | DEPENDENCY));
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

        if (parent != null) {
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

      return defineClass(name, bytecode, 0, bytecode.length);

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

    // Check if the bytecode for that class was generated already.
    byte[] bytecode = this.preCompiledClasses.remove(name);

    if (bytecode == null) {

      // Read, scan, parse and compile the right compilation unit.
      {
        Map<String, byte[]> generatedBytecodeMap = this.generateByteCodes(name);

        if (generatedBytecodeMap == null) {
          throw new ClassNotFoundException(name);
        }
        this.preCompiledClasses.putAll(generatedBytecodeMap);
      }

      // Now the bytecode for our class should be available.
      bytecode = this.preCompiledClasses.remove(name);

      if (bytecode == null) {
        return null;
      }
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

  private Map<String, byte[]> generateByteCodes(String name) throws ClassNotFoundException {

    if (this.javaSourceIClassLoader.loadIClass(Descriptor.fromClassName(name)) == null) {
      return null;
    }

    Map<String, byte[]> byteCodes = new HashMap<>();
    Set<UnitCompiler> compiledUnitCompilers = new HashSet<>();

    COMPILE_UNITS:
    for (; ; ) {

      for (UnitCompiler uc : this.javaSourceIClassLoader.getUnitCompilers()) {

        if (!compiledUnitCompilers.contains(uc)) {
          ClassFile[] cfs;

          try {
            cfs = uc.compileUnit(this.debug, this.debug, this.debug);

          } catch (CompileException ex) {
            throw new ClassNotFoundException(ex.getMessage(), ex);
          }

          for (ClassFile cf : cfs) {
            byteCodes.put(cf.getThisClassName(), cf.toByteArray());
          }
          compiledUnitCompilers.add(uc);
          continue COMPILE_UNITS;
        }
      }
      return byteCodes;
    }
  }
}
