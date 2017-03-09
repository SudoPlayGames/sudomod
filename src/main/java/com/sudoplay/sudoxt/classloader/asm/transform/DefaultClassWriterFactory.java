package com.sudoplay.sudoxt.classloader.asm.transform;

import org.objectweb.asm.ClassWriter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class DefaultClassWriterFactory implements
    IClassWriterFactory {

  @Override
  public ClassWriter create() {
    return new ClassWriter(ClassWriter.COMPUTE_FRAMES);
  }
}
