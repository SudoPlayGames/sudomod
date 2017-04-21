package com.sudoplay.sudoxt;

import com.sudoplay.sudoxt.classloader.ICompiler;
import com.sudoplay.sudoxt.classloader.SXClassLoader;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * https://github.com/igor-suhorukov/janino-commons-compiler-ecj/blob/master/src/main/java/org/codehaus/commons/compiler/jdk/JavaSourceClassLoader.java
 * <p>
 * Created by codetaylor on 4/13/2017.
 */
public class ECJCompiler implements
    ICompiler {

  private JavaCompiler javaCompiler;
  private JavaFileManager fileManager;
  private String optionalCharacterEncoding;
  private List<String> compilerOptions;
  private boolean debug;

  public ECJCompiler(boolean debug) {
    this.debug = debug;
  }

  @Override
  public void onClassLoaderInit(SXClassLoader classLoader, File[] sourcePath) {

    this.javaCompiler = new EclipseCompiler();

    this.fileManager = this.javaCompiler.getStandardFileManager(null, null, null);
    this.fileManager = new ByteArrayJavaFileManager<>(this.fileManager);
    this.fileManager = new FileInputJavaFileManager(
        this.fileManager,
        StandardLocation.SOURCE_PATH,
        JavaFileObject.Kind.SOURCE,
        sourcePath,
        this.optionalCharacterEncoding
    );

    this.compilerOptions = new ArrayList<>();

    if (this.debug) {
      this.compilerOptions.add("-g");
    }

  }

  @Override
  public byte[] compile(String name) throws ClassNotFoundException {

    byte[] result;
    int size;

    try {

      // Maybe the bytecode is already there, because the class was compiled as a side effect of a preceding
      // compilation.
      JavaFileObject classFileObject = this.fileManager
          .getJavaFileForInput(StandardLocation.CLASS_OUTPUT, name, JavaFileObject.Kind.CLASS);

      if (classFileObject == null) {

        JavaFileObject sourceFileObject;

        // Get the sourceFile.
        sourceFileObject = this.fileManager
            .getJavaFileForInput(StandardLocation.SOURCE_PATH, name, JavaFileObject.Kind.SOURCE);

        if (sourceFileObject == null) {
          throw new ECJCompilerException("Source for [" + name + "] not found");
        }

        // Compose the effective compiler options.
        List<String> options = new ArrayList<>(this.compilerOptions);

        // Run the compiler.
        JavaCompiler.CompilationTask task = this.javaCompiler
            .getTask(
                null, // out
                this.fileManager,
                null /*diagnostic -> {
                  if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    System.out.println("!!!");
                    System.out.println(diagnostic.getCode() + " - " + diagnostic.getLineNumber());
                    System.out.println(diagnostic.getMessage(null));
                    throw new ECJCompilerException(diagnostic.toString());
                  }
                }*/,
                options,
                null, // classes
                Collections.singleton(sourceFileObject) // compilationUnits
            );

        if (!task.call()) {
          throw new ClassNotFoundException(name + ": Compilation failed");
        }

        //name = name.replace(".", "/");

        classFileObject = this.fileManager
            .getJavaFileForInput(StandardLocation.CLASS_OUTPUT, name, JavaFileObject.Kind.CLASS);

        if (classFileObject == null) {
          throw new ClassNotFoundException(name + ": Class file not created by compilation");
        }

      }

      if (classFileObject instanceof ByteArrayJavaFileManager.ByteArrayJavaFileObject) {
        ByteArrayJavaFileManager.ByteArrayJavaFileObject bajfo;

        bajfo = (ByteArrayJavaFileManager.ByteArrayJavaFileObject) classFileObject;
        result = bajfo.toByteArray();
        //size = ba.length;

      } else {
        result = new byte[4096];
        size = 0;

        try (InputStream is = classFileObject.openInputStream()) {

          for (; ; ) {
            int res = is.read(result, size, result.length - size);

            if (res == -1) {
              break;
            }

            size += res;

            if (size == result.length) {
              byte[] tmp = new byte[2 * size];
              System.arraycopy(result, 0, tmp, 0, size);
              result = tmp;
            }

          }
        }
      }

    } catch (IOException | ECJCompilerException e) {
      throw new ClassNotFoundException(name, e);
    }

    return result;
  }

  private static class ECJCompilerException extends
      RuntimeException {

    /* package */ ECJCompilerException(String message) {
      super(message);
    }

  }

}
