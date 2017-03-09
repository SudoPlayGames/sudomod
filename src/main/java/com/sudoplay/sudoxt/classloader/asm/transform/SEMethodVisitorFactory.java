package com.sudoplay.sudoxt.classloader.asm.transform;

import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.JSRInlinerAdapter;

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
  public MethodVisitor create(
      MethodVisitor visitor,
      int access,
      String name,
      String desc,
      String signature,
      String[] exceptions
  ) {
    visitor = new JSRInlinerAdapter(visitor, access, name, desc, signature, exceptions);
    visitor = new SEMethodVisitor(visitor);
    visitor = new SEClassFilterMethodVisitor(
        visitor,
        this.classFilterPredicate,
        this.catchExceptionClassFilterPredicate
    );
    return visitor;
  }

}
