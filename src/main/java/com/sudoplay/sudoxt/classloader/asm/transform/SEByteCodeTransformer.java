package com.sudoplay.sudoxt.classloader.asm.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class SEByteCodeTransformer implements
    IByteCodeTransformer {

  private IClassReaderFactory classReaderFactory;
  private IClassVisitorFactory classVisitorFactory;
  private IClassWriterFactory classWriterFactory;
  private IClassReaderAcceptor classReaderAcceptor;
  private IByteCodePrinter byteCodePrinter;

  public SEByteCodeTransformer(
      IClassReaderFactory classReaderFactory,
      IClassVisitorFactory classVisitorFactory,
      IClassWriterFactory classWriterFactory,
      IClassReaderAcceptor classReaderAcceptor,
      IByteCodePrinter byteCodePrinter
  ) {
    this.classReaderFactory = classReaderFactory;
    this.classVisitorFactory = classVisitorFactory;
    this.classWriterFactory = classWriterFactory;
    this.classReaderAcceptor = classReaderAcceptor;
    this.byteCodePrinter = byteCodePrinter;
  }

  @Override
  public byte[] transform(byte[] bytecode) {

    ClassReader classReader;
    ClassWriter classWriter;
    ClassVisitor classVisitor;

    this.byteCodePrinter.print(bytecode);

    classReader = this.classReaderFactory.create(bytecode);
    classWriter = this.classWriterFactory.create();
    classVisitor = this.classVisitorFactory.create(classWriter);

    this.classReaderAcceptor.accept(classReader, classVisitor);

    bytecode = classWriter.toByteArray();

    this.byteCodePrinter.print(bytecode);

    return bytecode;
  }
}
