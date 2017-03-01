package com.sudoplay.sudoext.classloader.asm.transform;

import org.objectweb.asm.ClassVisitor;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IClassVisitorFactory {

  ClassVisitor create(ClassVisitor classVisitor);
}
