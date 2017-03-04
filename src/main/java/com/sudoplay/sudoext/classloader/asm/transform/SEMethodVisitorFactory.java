package com.sudoplay.sudoext.classloader.asm.transform;

import com.sudoplay.sudoext.classloader.filter.IClassFilterPredicate;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitorFactory implements IMethodVisitorFactory {

  private IClassFilterPredicate classFilterPredicate;
  private IClassFilterPredicate catchExceptionClassFilterPredicate;
  private boolean prohibitTryCatchBlocks;

  public SEMethodVisitorFactory(
      IClassFilterPredicate classFilterPredicate,
      IClassFilterPredicate catchExceptionClassFilterPredicate,
      boolean prohibitTryCatchBlocks
  ) {
    this.classFilterPredicate = classFilterPredicate;
    this.catchExceptionClassFilterPredicate = catchExceptionClassFilterPredicate;
    this.prohibitTryCatchBlocks = prohibitTryCatchBlocks;
  }

  @Override
  public MethodVisitor create(MethodVisitor methodVisitor) {
    MethodVisitor visitor;
    visitor = new SEMethodVisitor(methodVisitor);
    visitor = new SEClassFilterMethodVisitor(
        visitor,
        this.classFilterPredicate,
        this.catchExceptionClassFilterPredicate,
        this.prohibitTryCatchBlocks
    );
    return visitor;
  }

}
