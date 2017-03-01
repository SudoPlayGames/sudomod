package com.sudoplay.sudoext.classloader.asm.transform;

/**
 * Created by codetaylor on 2/28/2017.
 */
public interface IByteCodeTransformer {

  byte[] transform(byte[] bytecode);
}
