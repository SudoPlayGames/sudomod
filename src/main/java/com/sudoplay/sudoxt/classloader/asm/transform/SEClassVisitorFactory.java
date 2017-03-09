package com.sudoplay.sudoxt.classloader.asm.transform;

import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import org.objectweb.asm.ClassVisitor;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class SEClassVisitorFactory implements
    IClassVisitorFactory {

  private IMethodVisitorFactory methodVisitorFactory;
  private IClassFilterPredicate classFilterPredicate;

  public SEClassVisitorFactory(
      IMethodVisitorFactory methodVisitorFactory,
      IClassFilterPredicate classFilterPredicate
  ) {
    this.methodVisitorFactory = methodVisitorFactory;
    this.classFilterPredicate = classFilterPredicate;
  }

  @Override
  public ClassVisitor create(ClassVisitor classVisitor) {

    return new SEClassVisitor(
        classVisitor,
        this.methodVisitorFactory,
        this.classFilterPredicate
    );
  }
}
