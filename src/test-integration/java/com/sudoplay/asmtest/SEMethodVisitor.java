package com.sudoplay.asmtest;

import com.sudoplay.sudoext.security.IClassFilter;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * <p>
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitor extends
    MethodVisitor implements
    ISEMethodVisitor {

  private final IClassFilter[] classFilters;
  private LocalVariablesSorter localvariableSorter;

  private int arrayId;
  private int[] id;

  public SEMethodVisitor(
      MethodVisitor mv,
      IClassFilter[] classFilters
  ) {
    super(ASM5, mv);
    this.classFilters = classFilters;

    this.id = new int[8];
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {
    this.checkClassFilters(type);

    if (opcode == Opcodes.NEW) {
      this.injectCallback("callback_NEW");

    } else if (opcode == Opcodes.ANEWARRAY) {
      this.mv.visitInsn(Opcodes.DUP);
      this.injectCallback("callback_ANEWARRAY");
    }

    this.mv.visitTypeInsn(opcode, type);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    this.checkClassFilters(owner);

    if (!InjectedCallback.class.getName().replace(".", "/").equals(owner)) {

      if (opcode == Opcodes.INVOKESPECIAL) {
        this.injectCallback("callback_INVOKESPECIAL");

      } else if (opcode == Opcodes.INVOKEVIRTUAL) {
        this.injectCallback("callback_INVOKEVIRTUAL");

      } else if (opcode == Opcodes.INVOKESTATIC) {
        this.injectCallback("callback_INVOKESTATIC");

      } else if (opcode == Opcodes.INVOKEINTERFACE) {
        this.injectCallback("callback_INVOKEINTERFACE");
      }
    }

    this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
  }

  @Override
  public void visitJumpInsn(int opcode, Label label) {
    this.injectCallback("callback_JUMP");
    this.mv.visitJumpInsn(opcode, label);
  }

  @Override
  public void visitVarInsn(int opcode, int var) {

    if (opcode == Opcodes.RET) {
      this.injectCallback("callback_JUMP");
    }

    this.mv.visitVarInsn(opcode, var);
  }

  @Override
  public void visitIntInsn(int opcode, int operand) {

    if (opcode == Opcodes.NEWARRAY) {
      this.mv.visitInsn(Opcodes.DUP);
      this.injectCallback("callback_NEWARRAY", "(I)V");
    }

    this.mv.visitIntInsn(opcode, operand);
  }

  @Override
  public void visitMultiANewArrayInsn(String desc, int dims) {

    this.arrayId = this.localvariableSorter.newLocal(Type.getType(int[].class));
    System.out.println(this.arrayId);

    for (int i = 0; i < this.id.length; i++) {
      this.id[i] = this.localvariableSorter.newLocal(Type.INT_TYPE);
    }
    System.out.println(Arrays.toString(this.id));

    // create a new array to hold the local values
    this.mv.visitLdcInsn(this.id.length);
    this.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
    this.mv.visitVarInsn(Opcodes.ASTORE, this.arrayId);

    // store the values off the stack
    for (int i = 0; i < dims; i++) {
      this.mv.visitVarInsn(Opcodes.ISTORE, this.id[i]);
    }

    // store the local variables in the array
    for (int i = 0; i < dims; i++) {
      this.mv.visitVarInsn(Opcodes.ALOAD, this.arrayId);
      this.mv.visitLdcInsn(i);
      this.mv.visitVarInsn(Opcodes.ILOAD, this.id[i]);
      this.mv.visitInsn(Opcodes.IASTORE);
    }

    // call the callback
    this.mv.visitLdcInsn(dims);
    this.mv.visitVarInsn(Opcodes.ALOAD, this.arrayId);
    this.injectCallback("callback_MULTIANEWARRAY", "(I[I)V");

    // put the locals back on the stack
    for (int i = 0; i < dims; i++) {
      this.mv.visitVarInsn(Opcodes.ILOAD, this.id[i]);
    }

    // create the array
    this.mv.visitMultiANewArrayInsn(desc, dims);
    // throw new RestrictedUseException("Usage of multi-dimensional arrays is prohibited in extensions");
  }

  @Override
  public void visitInsn(int opcode) {

    if (opcode == Opcodes.ATHROW) {
      this.injectCallback("callback_ATHROW");
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
    this.injectCallback("callback_INVOKEDYNAMIC");
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
    throw new RestrictedUseException("Usage of try/catch block is prohibited in extensions");
  }

  private void injectCallback(String name) {
    this.injectCallback(name, "()V");
  }

  private void injectCallback(String name, String desc) {
    this.mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        InjectedCallback.class.getName().replace(".", "/"),
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
      throw new RestrictedUseException(String.format("Usage of class [%s] is prohibited in extensions", name));
    }
  }

  @Override
  public void setLocalVariableSorter(LocalVariablesSorter localVariableSorter) {
    this.localvariableSorter = localVariableSorter;
  }
}
