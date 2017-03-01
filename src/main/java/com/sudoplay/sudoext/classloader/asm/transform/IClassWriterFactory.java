package com.sudoplay.sudoext.classloader.asm.transform;

import org.objectweb.asm.ClassWriter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IClassWriterFactory {

  ClassWriter create();
}
