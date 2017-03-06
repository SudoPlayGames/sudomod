package com.sudoplay.sudoext.classloader.asm.transform;

import com.sudoplay.sudoext.classloader.asm.callback.InjectedCallback;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * <p>
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitor extends
    MethodVisitor {

  private int nonInitializedRegistrations;

  public SEMethodVisitor(
      MethodVisitor methodVisitor
  ) {
    super(ASM5, methodVisitor);
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {

    if (opcode == Opcodes.NEW) {
      this.mv.visitLdcInsn(type);
      // type

      this.injectCallback("callback_NEW", "(Ljava/lang/String;)V");
      // objectRef

      // don't register object here, wait until <init> in INVOKESPECIAL
      this.nonInitializedRegistrations += 1;

      this.mv.visitTypeInsn(opcode, type);

    } else if (opcode == Opcodes.ANEWARRAY) {
      // size

      this.mv.visitInsn(Opcodes.DUP);
      // size, size

      this.mv.visitIntInsn(Opcodes.BIPUSH, this.sizeOfAType(this.arrayTypeToOperand(type)));
      // size, size, memorySize

      this.injectCallback("callback_ANEWARRAY", "(II)V");
      // size

      this.mv.visitTypeInsn(opcode, type);
      // arrayRef

      this.mv.visitInsn(Opcodes.DUP);
      // arrayRef, arrayRef

      this.injectRegisterObject();
      // arrayRef

    } else {
      this.mv.visitTypeInsn(opcode, type);
    }
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

    if (opcode == Opcodes.INVOKESPECIAL) {
      // {params}

      this.mv.visitLdcInsn(owner);
      // {params}, owner

      this.mv.visitLdcInsn(name);
      // {params}, owner, name

      this.mv.visitLdcInsn(desc);
      // {params}, owner, name, desc

      this.injectCallback("callback_INVOKESPECIAL", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      // {params}

      this.mv.visitMethodInsn(opcode, owner, name, desc, itf);

      if ("<init>".equals(name) && this.nonInitializedRegistrations > 0) {
        this.nonInitializedRegistrations -= 1;

        this.mv.visitInsn(Opcodes.DUP);
        // objectRef, objectRef

        this.injectRegisterObject();
        // objectRef

      }

    } else if (opcode == Opcodes.INVOKEVIRTUAL) {
      // {params}

      this.mv.visitLdcInsn(owner);
      // {params}, owner

      this.mv.visitLdcInsn(name);
      // {params}, owner, name

      this.mv.visitLdcInsn(desc);
      // {params}, owner, name, desc

      this.injectCallback("callback_INVOKEVIRTUAL", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

      if ("clone".equals(name) && owner.startsWith("[")) {
        // obj

        this.mv.visitInsn(Opcodes.DUP);
        // obj, obj

        this.mv.visitTypeInsn(Opcodes.CHECKCAST, owner);
        // obj, arrayRef

        this.mv.visitInsn(Opcodes.ARRAYLENGTH);
        // obj, size

        this.mv.visitIntInsn(Opcodes.BIPUSH, this.sizeOfAType(-1)); // T_REF size
        // obj, size, memorySize

        this.injectCallback("callback_ANEWARRAY", "(II)V");
        // obj

        this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
        // obj

        this.mv.visitInsn(Opcodes.DUP);
        // obj, obj

        this.injectRegisterObject();
        // obj

      } else if ("newInstance".equals(name)) {

        if ("java/lang/Class".equals(owner) && "()Ljava/lang/Object;".equals(desc)) {
          this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
          // TODO?

        } else if ("java/lang/reflect/Constructor".equals(owner)
            && "([Ljava/lang/Object;)Ljava/lang/Object;".equals(desc)) {
          this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
          // TODO?
        }

      } else {
        this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      }

    } else if (opcode == Opcodes.INVOKESTATIC) {
      // {params}

      this.mv.visitLdcInsn(owner);
      // {params}, owner

      this.mv.visitLdcInsn(name);
      // {params}, owner, name

      this.mv.visitLdcInsn(desc);
      // {params}, owner, name, desc

      this.injectCallback("callback_INVOKESTATIC", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
      // {params}

      /**
       * Handle calls to Array.newInstance(Class,I) and Array.newInstance(Class,[I)
       */
      if ("java/lang/reflect/Array".equals(owner) && "newInstance".equals(name)) {

        if ("(Ljava/lang/Class;I)Ljava/lang/Object;".equals(desc)) {
          // class, size

          this.mv.visitInsn(Opcodes.DUP);
          // class, size, size

          this.mv.visitIntInsn(Opcodes.BIPUSH, this.sizeOfAType(-1)); // T_REF size
          // class, size, size, memorySize

          this.injectCallback("callback_ANEWARRAY", "(II)V");
          // class, size

          this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
          // arrayRef

          this.mv.visitInsn(Opcodes.DUP);
          // arrayRef, arrayRef

          this.injectRegisterObject();
          // arrayRef

        } else if ("(Ljava/lang/Class;[I)Ljava/lang/Object;".equals(desc)) {
          // class, arrayRef

          this.mv.visitInsn(Opcodes.DUP);
          // class, arrayRef, arrayRef

          this.mv.visitIntInsn(Opcodes.BIPUSH, this.sizeOfAType(-1)); // T_REF size
          // class, arrayRef, arrayRef, memorySize

          this.injectCallback("callback_MULTIANEWARRAY", "([II)V");
          // class, arrayRef

          this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
          // newArrayRef

          this.mv.visitInsn(Opcodes.DUP);
          // newArrayRef, newArrayRef

          this.injectRegisterObject();
          // newArrayRef
        }

      } else {
        this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      }

    } else if (opcode == Opcodes.INVOKEINTERFACE) {
      // {params}

      this.mv.visitLdcInsn(owner);
      // {params}, owner

      this.mv.visitLdcInsn(name);
      // {params}, owner, name

      this.mv.visitLdcInsn(desc);
      // {params}, owner, name, desc

      this.injectCallback("callback_INVOKEINTERFACE", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

      this.mv.visitMethodInsn(opcode, owner, name, desc, itf);

    } else {
      this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
    }
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
      // size

      this.mv.visitInsn(Opcodes.DUP);
      // size, size

      this.mv.visitLdcInsn(this.sizeOfAType(operand));
      // size, size, memorySize

      this.injectCallback("callback_NEWARRAY", "(II)V");
      // size

      this.mv.visitIntInsn(opcode, operand);
      // arrayRef

      this.mv.visitInsn(Opcodes.DUP);
      // arrayRef, arrayRef

      this.injectRegisterObject();
      // arrayRef

    } else {
      this.mv.visitIntInsn(opcode, operand);
    }
  }

  @Override
  public void visitMultiANewArrayInsn(String desc, int dimCount) {
    // dim1, dim2, dim3 ... dimN

    this.mv.visitIntInsn(Opcodes.BIPUSH, dimCount);
    // dim1, dim2, dim3 ... dimN, dimCount

    this.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
    // dim1, dim2, dim3 ... dimN, arrayRef

    for (int i = dimCount - 1; i >= 0; i--) {
      this.mv.visitInsn(Opcodes.DUP_X1);
      // dim1, dim2, dim3 ... dimN-1, arrayRef, dimN, arrayRef

      this.mv.visitInsn(Opcodes.SWAP);
      // dim1, dim2, dim3 ... dimN-1, arrayRef, arrayRef, dimN

      this.mv.visitIntInsn(Opcodes.BIPUSH, i);
      // dim1, dim2, dim3 ... dimN-1, arrayRef, arrayRef, dimN, index

      this.mv.visitInsn(Opcodes.SWAP);
      // dim1, dim2, dim3 ... dimN-1, arrayRef, arrayRef, index, dimN

      this.mv.visitInsn(Opcodes.IASTORE);
      // dim1, dim2, dim3 ... dimN-1, arrayRef
    }
    // arrayRef

    this.mv.visitInsn(Opcodes.DUP);
    // arrayRef, arrayRef

    this.mv.visitIntInsn(Opcodes.BIPUSH, sizeOfAType(arrayTypeToOperand(desc)));
    // arrayRef, arrayRef, memorySize

    this.injectCallback("callback_MULTIANEWARRAY", "([II)V");
    // arrayRef

    for (int i = 0; i < dimCount; i++) {
      this.mv.visitInsn(Opcodes.DUP);
      // arrayRef, arrayRef

      this.mv.visitIntInsn(Opcodes.BIPUSH, i);
      // arrayRef, arrayRef, index

      this.mv.visitInsn(Opcodes.IALOAD);
      // arrayRef, value

      this.mv.visitInsn(Opcodes.SWAP);
      // value, arrayRef
    }
    // dim1, dim2, dim3 ... dimN, arrayRef

    this.mv.visitInsn(Opcodes.POP);
    // dim1, dim2, dim3 ... dimN

    this.mv.visitMultiANewArrayInsn(desc, dimCount);
    // arrayRef

    this.mv.visitInsn(Opcodes.DUP);
    // arrayRef, arrayRef

    this.injectRegisterObject();
  }

  @Override
  public void visitInsn(int opcode) {

    if (opcode == Opcodes.ATHROW) {
      this.injectCallback("callback_ATHROW");
    }

    this.mv.visitInsn(opcode);
  }

  @Override
  public void visitLdcInsn(Object cst) {

    if (cst instanceof String) {
      this.mv.visitLdcInsn(cst);
      this.injectCallback("callback_LDC", "(Ljava/lang/String;)V");
    }

    this.mv.visitLdcInsn(cst);
  }

  @Override
  public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
    // {params}

    this.mv.visitLdcInsn(name);
    // {params}, name

    this.mv.visitLdcInsn(desc);
    // {params}, name, desc

    this.injectCallback("callback_INVOKEDYNAMIC", "(Ljava/lang/String;Ljava/lang/String;)V");
    this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
  }

  @Override
  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    this.injectCallback("callback_TRYCATCH", "(Ljava/lang/String;)V");
  }

  private void injectCallback(String name) {
    this.injectCallback(name, "()V");
  }

  private void injectRegisterObject() {
    this.injectCallback("registerObject", "(Ljava/lang/Object;)V");
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

  private int arrayTypeToOperand(String typeName) {

    int i = typeName.lastIndexOf('[');

    if (i + 2 != typeName.length()) {
      return -1; // T_REF
    }

    switch (typeName.charAt(i + 1)) {

      case 'Z':
        return Opcodes.T_BOOLEAN;

      case 'C':
        return Opcodes.T_CHAR;

      case 'F':
        return Opcodes.T_FLOAT;

      case 'D':
        return Opcodes.T_DOUBLE;

      case 'B':
        return Opcodes.T_BYTE;

      case 'S':
        return Opcodes.T_SHORT;

      case 'I':
        return Opcodes.T_INT;

      case 'J':
        return Opcodes.T_LONG;

      default:
        return -1; // T_REF
    }
  }

  private int sizeOfAType(int operand) {

    switch (operand) {

      case Opcodes.T_BYTE:
      case Opcodes.T_BOOLEAN:
        return 1;

      case Opcodes.T_SHORT:
      case Opcodes.T_CHAR:
        return 2;

      case Opcodes.T_INT:
      case Opcodes.T_FLOAT:
        return 4;

      case -1: // T_REF
      case Opcodes.T_LONG:
      case Opcodes.T_DOUBLE:
        return 8;

      default:
        return 1;
    }
  }
}
