package com.sudoplay.asmtest;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.util.CheckClassAdapter;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEClassVisitor extends
    ClassVisitor {

  private IMethodVisitorFactory methodVisitorFactory;
  private String className;

  public SEClassVisitor(
      ClassVisitor classVisitor,
      IMethodVisitorFactory methodVisitorFactory
  ) {
    super(ASM5, classVisitor);
    this.methodVisitorFactory = methodVisitorFactory;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);
    this.className = name;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

    MethodVisitor methodVisitor;

    if ("finalize".equals(name)) {
      throw new RestrictedUseException("Usage of the finalize() method is prohibited in extensions");
    }

    methodVisitor = this.cv.visitMethod(access, name, desc, signature, exceptions);

    if (methodVisitor != null) {

      ISEMethodVisitor visitor = this.methodVisitorFactory.create(methodVisitor);

      methodVisitor = (MethodVisitor) visitor;

      AnalyzerAdapter analyzerAdapter = new AnalyzerAdapter(this.className, access, name, desc, methodVisitor);

      LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(access, desc, analyzerAdapter);

      visitor.setLocalVariableSorter(localVariablesSorter);

      return localVariablesSorter;
    }

    return null;
  }
}
