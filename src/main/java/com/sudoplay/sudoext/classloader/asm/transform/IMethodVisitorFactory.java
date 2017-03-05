package com.sudoplay.sudoext.classloader.asm.transform;

import org.objectweb.asm.MethodVisitor;

/**
 * Created by codetaylor on 2/26/2017.
 */
public interface IMethodVisitorFactory {

  MethodVisitor create(MethodVisitor methodVisitor);
}