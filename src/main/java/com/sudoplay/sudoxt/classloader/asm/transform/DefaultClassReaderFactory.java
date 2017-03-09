package com.sudoplay.sudoxt.classloader.asm.transform;

import org.objectweb.asm.ClassReader;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class DefaultClassReaderFactory implements
    IClassReaderFactory {

  @Override
  public ClassReader create(byte[] bytecode) {
    return new ClassReader(bytecode);
  }
}
