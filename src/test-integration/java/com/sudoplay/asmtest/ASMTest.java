package com.sudoplay.asmtest;

import com.sudoplay.sudoext.security.IClassFilter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class ASMTest {

  public ASMTest() throws Exception {
    String classToModifyNameString = "com.sudoplay.asmtest.TestFile";

    DebugCallbackDelegate delegate = new DebugCallbackDelegate();
    InjectedCallback.DELEGATE = delegate;

    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

    ClassVisitor classVisitor = new SEClassVisitor(
        classWriter,
        new SEMethodVisitorFactory(
            new IClassFilter[]{
                name -> {
                  switch (name) {
                    case "I": // int
                    case "[I": // int array
                    case "[[[[[I": // for testing
                    case "java.lang.Object":
                    case "java.lang.String":
                    case "java.lang.StringBuilder":
                    case "java.lang.Integer":
                    case "java.io.IOException":
                    case "java.lang.System":
                    case "java.io.PrintStream":
                      return true;
                  }
                  return name.startsWith("com.sudoplay.asmtest.");
                }
            }
        )
    );

    //CheckClassAdapter checkClassAdapter = new CheckClassAdapter(classVisitor, true);

    ClassReader classReader = new ClassReader(getClass()
        .getResourceAsStream("/" + classToModifyNameString.replace('.', '/') + ".class"));

    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

    byte[] bytecode = classWriter.toByteArray();

    ClassReader printClassReader = new ClassReader(bytecode);
    printClassReader.accept(new TraceClassVisitor(new PrintWriter(System.out)), 0);

    Class classModified = loadClass(classToModifyNameString, bytecode);

    try {

      TestFile testFile = (TestFile) classModified.newInstance();
      System.out.println(testFile.getIntegersString());

      delegate.report();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // From: http://asm.ow2.org/doc/faq.html#Q5
  private Class loadClass(String className, byte[] b) {
    // override classDefine (as it is protected) and define the class.
    Class clazz = null;
    try {
      ClassLoader loader = ClassLoader.getSystemClassLoader();
      Class cls = Class.forName("java.lang.ClassLoader");

      //noinspection unchecked
      Method method = cls.getDeclaredMethod(
          "defineClass", String.class, byte[].class, int.class, int.class
      );

      // protected method invocation
      method.setAccessible(true);

      try {
        Object[] args = new Object[]{className, b, new Integer(0), new Integer(b.length)};
        clazz = (Class) method.invoke(loader, args);

      } finally {
        //noinspection ThrowFromFinallyBlock
        method.setAccessible(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return clazz;
  }

  public static void main(String[] args) {

    try {
      new ASMTest();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
