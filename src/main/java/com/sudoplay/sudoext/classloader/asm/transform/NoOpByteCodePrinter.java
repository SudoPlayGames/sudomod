package com.sudoplay.sudoext.classloader.asm.transform;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class NoOpByteCodePrinter implements
    IByteCodePrinter {

  @Override
  public void print(byte[] bytecode) {
    // no-op
  }
}
