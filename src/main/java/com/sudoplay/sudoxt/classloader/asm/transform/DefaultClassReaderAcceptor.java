package com.sudoplay.sudoxt.classloader.asm.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class DefaultClassReaderAcceptor implements
    IClassReaderAcceptor {

  @Override
  public void accept(ClassReader classReader, ClassVisitor classVisitor) {
    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
  }
}
