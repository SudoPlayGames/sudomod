package com.sudoplay.sudoext.classloader.asm.visitor;

import org.objectweb.asm.MethodVisitor;

/**
 * Created by codetaylor on 2/26/2017.
 */
public interface IMethodVisitorFactory {

  MethodVisitor create(MethodVisitor methodVisitor);
}
