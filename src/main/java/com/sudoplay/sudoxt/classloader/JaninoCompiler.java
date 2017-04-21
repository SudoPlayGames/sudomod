package com.sudoplay.sudoxt.classloader;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.Descriptor;
import org.codehaus.janino.UnitCompiler;
import org.codehaus.janino.util.ClassFile;
import org.codehaus.janino.util.resource.PathResourceFinder;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 4/13/2017.
 */
public class JaninoCompiler implements
    ICompiler {

  private SXJavaSourceIClassLoader javaSourceIClassLoader;
  private final Map<String, byte[]> preCompiledClasses;
  private final boolean debug;

  public JaninoCompiler(boolean debug) {
    this.debug = debug;
    this.preCompiledClasses = new HashMap<>();
  }

  @Override
  public void onClassLoaderInit(SXClassLoader classLoader, File[] sourcePath) {
    PathResourceFinder sourceFinder = new PathResourceFinder(sourcePath);

    this.javaSourceIClassLoader = new SXJavaSourceIClassLoader(
        sourceFinder,
        null,
        new SXIClassLoader(classLoader)
    );
  }

  @Override
  public byte[] compile(String name) throws ClassNotFoundException {

    byte[] bytecode;

    // Check if the bytecode for that class was generated already.
    bytecode = this.preCompiledClasses.remove(name);

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

    }

    return bytecode;
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
