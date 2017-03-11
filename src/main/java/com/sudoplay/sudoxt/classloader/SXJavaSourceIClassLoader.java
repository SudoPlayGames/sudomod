/*
 * Janino - An embedded Java[TM] compiler
 *
 * Copyright (c) 2001-2010, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sudoplay.sudoxt.classloader;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.nullanalysis.Nullable;
import org.codehaus.janino.*;
import org.codehaus.janino.util.ClassFile;
import org.codehaus.janino.util.resource.Resource;
import org.codehaus.janino.util.resource.ResourceFinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Modified version of {@link JavaSourceIClassLoader}.
 * <p>
 * Created by codetaylor on 3/11/2017.
 */
/* package */ class SXJavaSourceIClassLoader extends IClassLoader {

  private static final Logger LOGGER = Logger.getLogger(SXJavaSourceIClassLoader.class.getName());

  private ResourceFinder sourceFinder;

  @Nullable
  private String optionalCharacterEncoding;

  /**
   * Collection of parsed compilation units.
   */
  private final Set<UnitCompiler> unitCompilers = new HashSet<>();

  /* package */ SXJavaSourceIClassLoader(
      ResourceFinder sourceFinder,
      @Nullable String optionalCharacterEncoding,
      IClassLoader parentIClassLoader
  ) {
    super(parentIClassLoader);

    this.sourceFinder = sourceFinder;
    this.optionalCharacterEncoding = optionalCharacterEncoding;
    super.postConstruct();
  }

  /**
   * Returns the set of {@link UnitCompiler}s that were created so far.
   */
  /* package */ Set<UnitCompiler> getUnitCompilers() {
    return this.unitCompilers;
  }

  @Override
  protected IClass requireType(String descriptor) {
    IClass result;

    try {
      result = this.optionalParentIClassLoader.loadIClass(descriptor);

    } catch (ClassNotFoundException e) {
      throw new AssertionError(e);
    }

    if (result != null) {
      return result;
    }

    throw new AssertionError("Required type \"" + descriptor + "\" not found");
  }

  @Override
  public IClass loadIClass(String fieldDescriptor) throws ClassNotFoundException {
    LOGGER.entering(null, "loadIClass", fieldDescriptor);

    if (Descriptor.isPrimitive(fieldDescriptor)) {

      switch (fieldDescriptor) {
        case Descriptor.VOID:
          return IClass.VOID;
        case Descriptor.BYTE:
          return IClass.BYTE;
        case Descriptor.CHAR:
          return IClass.CHAR;
        case Descriptor.DOUBLE:
          return IClass.DOUBLE;
        case Descriptor.FLOAT:
          return IClass.FLOAT;
        case Descriptor.INT:
          return IClass.INT;
        case Descriptor.LONG:
          return IClass.LONG;
        case Descriptor.SHORT:
          return IClass.SHORT;
        case Descriptor.BOOLEAN:
          return IClass.BOOLEAN;
        default:
          return null;
      }
    }

    // We need to synchronize here because "unloadableIClasses" and
    // "loadedIClasses" are unsynchronized containers.
    IClass result = null;

    synchronized (this) {
      // Class could not be loaded before?
      if (!this.unloadableIClasses.contains(fieldDescriptor)) {

        // Class already loaded?
        result = this.loadedIClasses.get(fieldDescriptor);

        // Special handling for array types.
        if (result == null && Descriptor.isArrayReference(fieldDescriptor)) {

          // Load the component type.
          IClass componentIClass = this.loadIClass(
              Descriptor.getComponentDescriptor(fieldDescriptor)
          );
          if (componentIClass == null) return null;

          // Now get and define the array type.
          result = componentIClass.getArrayIClass(this.TYPE_java_lang_Object);
          this.loadedIClasses.put(fieldDescriptor, result);
        }

        // Load the class through the {@link #findIClass(String)} method implemented by the
        // derived class.
        if (result == null) {
          LOGGER.log(Level.FINE, "About to call \"findIClass({0})\"", fieldDescriptor);
          result = this.findIClass(fieldDescriptor);
        }

        // Ask parent IClassLoader last.
        if (result == null && this.optionalParentIClassLoader != null) {
          result = this.optionalParentIClassLoader.loadIClass(fieldDescriptor);
        }

        if (result == null) {
          this.unloadableIClasses.add(fieldDescriptor);
        }

        if (result != null && !result.getDescriptor().equalsIgnoreCase(fieldDescriptor)) {
          throw new JaninoRuntimeException(
              "\"findIClass()\" returned \""
                  + result.getDescriptor()
                  + "\" instead of \""
                  + fieldDescriptor
                  + "\""
          );
        }
      }
    }

    LOGGER.exiting(null, "loadIClass", result);
    return result;
  }

  /**
   * @param fieldDescriptor Field descriptor of the {@link IClass} to load, e.g. "Lpkg1/pkg2/Outer$Inner;"
   * @throws ClassNotFoundException An exception was raised while loading the {@link IClass}
   */
  @Override
  @Nullable
  public IClass findIClass(final String fieldDescriptor) throws ClassNotFoundException {
    LOGGER.entering(null, "findIClass", fieldDescriptor);

    // Class type.
    String className = Descriptor.toClassName(fieldDescriptor); // E.g. "pkg1.pkg2.Outer$Inner"
    LOGGER.log(Level.FINE, "className={0}", className);

    // Do not attempt to load classes from package "java".
    if (className.startsWith("java.")) return null;

    // Determine the name of the top-level class.
    String topLevelClassName;
    {
      int idx = className.indexOf('$');
      topLevelClassName = idx == -1 ? className : className.substring(0, idx);
    }

    // Check the already-parsed compilation units.
    for (UnitCompiler uc : this.unitCompilers) {
      IClass res = uc.findClass(topLevelClassName);

      if (res != null) {

        if (!className.equals(topLevelClassName)) {
          res = uc.findClass(className);

          if (res == null) {
            return null;
          }
        }
        this.defineIClass(res);
        return res;
      }
    }

    try {
      Java.CompilationUnit cu = this.findCompilationUnit(className);
      if (cu == null) return null;

      UnitCompiler uc = new UnitCompiler(cu, this);

      // Remember compilation unit for later compilation.
      this.unitCompilers.add(uc);

      // Find the class/interface declaration in the compiled unit.
      IClass res = uc.findClass(className);

      if (res == null) {

        if (className.equals(topLevelClassName)) {
          throw new CompileException(
              "Compilation unit '" + className + "' does not declare a class with the same name",
              null
          );
        }
        return null;
      }
      this.defineIClass(res);
      return res;

    } catch (IOException | CompileException e) {
      throw new ClassNotFoundException("Parsing compilation unit '" + className + "'", e);
    }
  }

  /**
   * Finds the Java source file for the named class through the configured 'source resource finder' and parses it.
   *
   * @return {@code null} iff the source file could not be found
   */
  @Nullable
  private Java.CompilationUnit findCompilationUnit(String className) throws IOException, CompileException {

    // Find source file.
    Resource sourceResource = this.sourceFinder.findResource(ClassFile.getSourceResourceName(className));
    LOGGER.log(Level.FINE, "sourceResource={0}", sourceResource);
    if (sourceResource == null) return null;

    // Scan and parse the source file.
    InputStream inputStream = sourceResource.open();
    try {
      Scanner scanner = new Scanner(
          sourceResource.getFileName(),
          inputStream,
          this.optionalCharacterEncoding
      );

      Parser parser = new Parser(scanner);
      return parser.parseCompilationUnit();

    } finally {

      try {
        inputStream.close();

      } catch (IOException ex) {
        //
      }
    }
  }
}