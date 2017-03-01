package com.sudoplay.sudoext.classloader.asm.visitor;

import com.sudoplay.sudoext.classloader.IClassFilter;
import com.sudoplay.sudoext.classloader.asm.exception.RestrictedUseException;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * <p>
 * Created by codetaylor on 2/26/2017.
 */
public class SEClassFilterMethodVisitor extends
    MethodVisitor {

  private final IClassFilter[] classFilters;
  private final IClassFilter[] catchExceptionClassFilters;

  public SEClassFilterMethodVisitor(
      MethodVisitor mv,
      IClassFilter[] classFilters,
      IClassFilter[] catchExceptionClassFilters
  ) {
    super(ASM5, mv);
    this.classFilters = classFilters;
    this.catchExceptionClassFilters = catchExceptionClassFilters;
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {
    this.checkClassFilters(type);
    this.mv.visitTypeInsn(opcode, type);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    this.checkClassFilters(owner);
    this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
  }

  @Override
  public void visitMultiANewArrayInsn(String desc, int dimCount) {
    this.checkClassFilters(desc);
    this.mv.visitMultiANewArrayInsn(desc, dimCount);
  }

  @Override
  public void visitParameter(String name, int access) {
    this.checkClassFilters(name);
    this.mv.visitParameter(name, access);
  }

  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    this.checkClassFilters(owner);
    this.mv.visitFieldInsn(opcode, owner, name, desc);
  }

  @Override
  public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
    this.checkClassFilters(desc);
    this.mv.visitLocalVariable(name, desc, signature, start, end, index);
  }

  @Override
  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    this.checkCatchExceptionClassFilters(type);
  }

  private void checkClassFilters(String name) {
    this.checkClassFilters(
        name,
        this.classFilters,
        "Usage of class [%s] is prohibited"
    );
  }

  private void checkCatchExceptionClassFilters(String name) {
    this.checkClassFilters(
        name,
        this.catchExceptionClassFilters,
        "Usage of class [%s] is prohibited in try/catch blocks"
    );
  }

  private void checkClassFilters(String name, IClassFilter[] classFilters, String format) {
    boolean isAllowed = false;

    name = parseClassName(name);
    name = name.replaceAll("/", ".");

    for (IClassFilter classFilter : classFilters) {

      if (classFilter.isAllowed(name)) {
        isAllowed = true;
        break;
      }
    }

    if (!isAllowed) {
      throw new RestrictedUseException(String.format(format, name));
    }
  }

  @NotNull
  private String parseClassName(String name) {
    char[] chars = name.toCharArray();
    int index;

    for (index = 0; index < chars.length; index++) {

      if (chars[index] == '[') {
        continue;

      }
      break;
    }

    if (name.startsWith("L") && name.endsWith(";")) {
      name = name.substring(index + 1, name.length() - 1);

    } else {
      name = name.substring(index);
    }

    return name;
  }
}
