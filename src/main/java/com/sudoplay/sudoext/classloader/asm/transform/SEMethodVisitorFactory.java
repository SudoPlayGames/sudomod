package com.sudoplay.sudoext.classloader.asm.transform;

import com.sudoplay.sudoext.classloader.filter.IClassFilterPredicate;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitorFactory implements IMethodVisitorFactory {

  private IClassFilterPredicate classFilterPredicate;
  private IClassFilterPredicate catchExceptionClassFilterPredicate;

  public SEMethodVisitorFactory(
      IClassFilterPredicate classFilterPredicate,
      IClassFilterPredicate catchExceptionClassFilterPredicate
  ) {
    this.classFilterPredicate = classFilterPredicate;
    this.catchExceptionClassFilterPredicate = catchExceptionClassFilterPredicate;
  }

  @Override
  public MethodVisitor create(MethodVisitor methodVisitor) {
    MethodVisitor visitor;
    visitor = new SEMethodVisitor(methodVisitor);
    visitor = new SEClassFilterMethodVisitor(
        visitor,
        this.classFilterPredicate,
        this.catchExceptionClassFilterPredicate
    );
    return visitor;
  }

}
