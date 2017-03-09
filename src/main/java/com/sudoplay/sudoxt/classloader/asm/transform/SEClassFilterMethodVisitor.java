package com.sudoplay.sudoxt.classloader.asm.transform;

import com.sudoplay.sudoxt.classloader.asm.exception.RestrictedUseException;
import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * <p>
 * Created by codetaylor on 2/26/2017.
 */
public class SEClassFilterMethodVisitor extends
    MethodVisitor {

  private final IClassFilterPredicate classFilterPredicate;
  private final IClassFilterPredicate catchExceptionClassFilterPredicate;

  public SEClassFilterMethodVisitor(
      MethodVisitor mv,
      IClassFilterPredicate classFilterPredicate,
      IClassFilterPredicate catchExceptionClassFilterPredicate
  ) {
    super(ASM5, mv);
    this.classFilterPredicate = classFilterPredicate;
    this.catchExceptionClassFilterPredicate = catchExceptionClassFilterPredicate;
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
        this.classFilterPredicate,
        "Usage of class [%s] is prohibited"
    );
  }

  private void checkCatchExceptionClassFilters(String name) {
    this.checkClassFilters(
        name,
        this.catchExceptionClassFilterPredicate,
        "Usage of class [%s] is prohibited in try/catch blocks"
    );
  }

  private void checkClassFilters(String name, IClassFilterPredicate predicate, String format) {

    if (!predicate.isAllowed(name)) {
      throw new RestrictedUseException(String.format(format, name));
    }
  }


}
