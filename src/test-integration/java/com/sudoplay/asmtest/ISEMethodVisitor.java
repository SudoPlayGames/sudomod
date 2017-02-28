package com.sudoplay.asmtest;

import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 * Created by codetaylor on 2/27/2017.
 */
public interface ISEMethodVisitor {

  void setLocalVariableSorter(LocalVariablesSorter localVariableSorter);
}
