package com.sudoplay.asmtest;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEClassVisitor extends
    ClassVisitor {

  private IMethodVisitorFactory methodVisitorFactory;

  public SEClassVisitor(ClassVisitor cv, IMethodVisitorFactory methodVisitorFactory) {
    super(ASM5, cv);
    this.methodVisitorFactory = methodVisitorFactory;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

    MethodVisitor methodVisitor;

    methodVisitor = this.cv.visitMethod(access, name, desc, signature, exceptions);

    if (methodVisitor != null) {
      methodVisitor = this.methodVisitorFactory.create(methodVisitor);
    }

    return methodVisitor;
  }
}
