package com.sudoplay.sudoxt.classloader.asm.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IClassReaderAcceptor {

  void accept(ClassReader classReader, ClassVisitor classVisitor);
}
