package com.sudoplay.sudoxt.classloader.asm.transform;

/**
 * Created by codetaylor on 2/28/2017.
 */
public class NoOpByteCodeTransformer implements
    IByteCodeTransformer {

  @Override
  public byte[] transform(byte[] bytecode) {
    return bytecode;
  }
}
