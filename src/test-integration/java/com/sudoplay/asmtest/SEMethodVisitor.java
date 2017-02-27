package com.sudoplay.asmtest;

import com.sudoplay.sudoext.security.IClassFilter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.security.AccessControlException;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * <p>
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitor extends
    MethodVisitor {

  private final IClassFilter[] classFilters;

  public SEMethodVisitor(
      MethodVisitor mv,
      IClassFilter[] classFilters
  ) {
    super(ASM5, mv);
    this.classFilters = classFilters;
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {
    this.checkClassFilters(type);

    if (opcode == Opcodes.NEW) {
      this.injectInstrumentCallback("callback_NEW");

    } else if (opcode == Opcodes.ANEWARRAY) {
      this.injectInstrumentCallback("callback_ANEWARRAY");
    }

    this.mv.visitTypeInsn(opcode, type);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    this.checkClassFilters(owner);

    if (opcode == Opcodes.INVOKESPECIAL) {
      this.injectInstrumentCallback("callback_INVOKESPECIAL");

    } else if (opcode == Opcodes.INVOKEVIRTUAL) {
      this.injectInstrumentCallback("callback_INVOKEVIRTUAL");

    } else if (opcode == Opcodes.INVOKESTATIC) {
      this.injectInstrumentCallback("callback_INVOKESTATIC");

    } else if (opcode == Opcodes.INVOKEINTERFACE) {
      this.injectInstrumentCallback("callback_INVOKEINTERFACE");
    }

    this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
  }

  @Override
  public void visitJumpInsn(int opcode, Label label) {
    this.injectInstrumentCallback("callback_JUMP");
    this.mv.visitJumpInsn(opcode, label);
  }

  @Override
  public void visitVarInsn(int opcode, int var) {

    if (opcode == Opcodes.RET) {
      this.injectInstrumentCallback("callback_JUMP");
    }

    this.mv.visitVarInsn(opcode, var);
  }

  @Override
  public void visitIntInsn(int opcode, int operand) {

    if (opcode == Opcodes.NEWARRAY) {
      //this.mv.visitLdcInsn(operand);
      this.mv.visitInsn(Opcodes.DUP);
      this.injectInstrumentCallback("callback_NEWARRAY", "(I)V");
    }

    this.mv.visitIntInsn(opcode, operand);
  }

  @Override
  public void visitMultiANewArrayInsn(String desc, int dims) {
    this.checkClassFilters(desc);
    this.mv.visitLdcInsn(dims);
    this.injectInstrumentCallback("callback_MULTIANEWARRAYINSN", "(I)V");
    this.mv.visitMultiANewArrayInsn(desc, dims);
  }

  @Override
  public void visitInsn(int opcode) {

    if (opcode == Opcodes.ATHROW) {
      this.injectInstrumentCallback("callback_ATHROW");
    }

    this.mv.visitInsn(opcode);
  }

  @Override
  public void visitParameter(String name, int access) {
    this.checkClassFilters(name);
    this.mv.visitParameter(name, access);
  }

  @Override
  public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
    this.injectInstrumentCallback("callback_INVOKEDYNAMIC");
    this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
  }

  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    this.checkClassFilters(owner);
    this.mv.visitFieldInsn(opcode, owner, name, desc);
  }

  @Override
  public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {

    if (desc.startsWith("L") && desc.endsWith(";")) {
      this.checkClassFilters(desc.substring(1, desc.length() - 1));

    } else if (desc.startsWith("[L") && desc.endsWith(";")) {
      this.checkClassFilters(desc.substring(2, desc.length() - 1));

    } else {
      this.checkClassFilters(desc);
    }

    this.mv.visitLocalVariable(name, desc, signature, start, end, index);
  }

  @Override
  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {

    if ("java/lang/Exception".equals(type)) {
      throw new AccessControlException("Usage of [java.lang.Exception] in try/catch block is restricted");
    }

    this.mv.visitTryCatchBlock(start, end, handler, type);
  }

  private void injectInstrumentCallback(String name) {
    this.injectInstrumentCallback(name, "()V");
  }

  private void injectInstrumentCallback(String name, String desc) {
    this.mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        Instrument.class.getName().replace(".", "/"),
        name,
        desc,
        false
    );
  }

  private void checkClassFilters(String name) {
    boolean isAllowed = false;

    name = name.replaceAll("/", ".");

    for (IClassFilter classFilter : this.classFilters) {

      if (classFilter.isAllowed(name)) {
        isAllowed = true;
        break;
      }
    }

    if (!isAllowed) {
      throw new AccessControlException(String.format("Usage of class [%s] restricted", name));
    }
  }

}
