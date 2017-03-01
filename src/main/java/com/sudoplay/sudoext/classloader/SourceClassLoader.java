package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.security.ISandboxClassLoader;
import org.codehaus.commons.nullanalysis.Nullable;
import org.codehaus.janino.JaninoRuntimeException;
import org.codehaus.janino.JavaSourceClassLoader;
import org.codehaus.janino.JavaSourceIClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 2/21/2017.
 */
/* package */ class SourceClassLoader extends
    JavaSourceClassLoader implements
    IClassLoader,
    ISandboxClassLoader {

  private static final Logger LOG = LoggerFactory.getLogger(SourceClassLoader.class);

  private final Map<String, byte[]> precompiledClasses = new HashMap<>();

  private IByteCodeTransformer byteCodeTransformer;

  /* package */ SourceClassLoader(
      ClassLoader parentClassLoader,
      JavaSourceIClassLoader iClassLoader,
      IByteCodeTransformer byteCodeTransformer
  ) {
    super(parentClassLoader, iClassLoader);

    SecurityManager security = System.getSecurityManager();

    if (security != null) {
      security.checkCreateClassLoader();
    }

    this.byteCodeTransformer = byteCodeTransformer;
  }

  @Override
  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {

      Class<?> c = findLoadedClass(name);

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
          c = findClass(name);
        }
      }

      return c;
    }
  }

  /**
   * Implementation of {@link ClassLoader#findClass(String)}.
   *
   * @throws ClassNotFoundException
   */
  @Override
  protected Class<?> findClass(@Nullable String name) throws ClassNotFoundException {
    assert name != null;

    // Check if the bytecode for that class was generated already.
    byte[] bytecode = this.precompiledClasses.remove(name);

    if (bytecode == null) {

      // Read, scan, parse and compile the right compilation unit.
      {
        Map<String, byte[]> generatedBytecodeMap = this.generateBytecodes(name);

        if (generatedBytecodeMap == null) {
          throw new ClassNotFoundException(name);
        }
        this.precompiledClasses.putAll(generatedBytecodeMap);
      }

      // Now the bytecode for our class should be available.
      bytecode = this.precompiledClasses.remove(name);

      if (bytecode == null) {
        throw new JaninoRuntimeException(
            "SNO: Scanning, parsing and compiling class \""
                + name
                + "\" did not create a class file!?"
        );
      }
    }

    try {
      bytecode = this.byteCodeTransformer.transform(new ByteArrayInputStream(bytecode));
      return this.defineClass(name, bytecode, 0, bytecode.length);

    } catch (IOException e) {
      throw new ClassNotFoundException(name, e);
    }
  }
}
