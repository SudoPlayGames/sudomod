package com.sudoplay.sudoxt.classloader.asm.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class StdOutByteCodePrinter implements
    IByteCodePrinter {

  @Override
  public void print(byte[] bytecode) {
    System.out.println("--- [Start ByteCode] ---");
    ClassReader printClassReader = new ClassReader(bytecode);
    printClassReader.accept(new TraceClassVisitor(new PrintWriter(System.out)), 0);
    System.out.println("--- [End ByteCode] ---");
  }
}
