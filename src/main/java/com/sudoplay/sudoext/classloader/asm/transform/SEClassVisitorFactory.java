package com.sudoplay.sudoext.classloader.asm.transform;

import org.objectweb.asm.ClassVisitor;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class SEClassVisitorFactory implements
    IClassVisitorFactory {

  private IMethodVisitorFactory methodVisitorFactory;

  public SEClassVisitorFactory(
      IMethodVisitorFactory methodVisitorFactory
  ) {
    this.methodVisitorFactory = methodVisitorFactory;
  }

  @Override
  public ClassVisitor create(ClassVisitor classVisitor) {

    return new SEClassVisitor(
        classVisitor,
        this.methodVisitorFactory
    );
  }
}
