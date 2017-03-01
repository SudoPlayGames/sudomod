package com.sudoplay.sudoext.classloader.asm.transform;

import com.sudoplay.sudoext.classloader.filter.IClassFilter;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitorFactory implements IMethodVisitorFactory {

  private IClassFilter[] classFilters;
  private IClassFilter[] catchExceptionClassFilter;

  public SEMethodVisitorFactory(
      IClassFilter[] classFilters,
      IClassFilter[] catchExceptionClassFilter
  ) {
    this.classFilters = classFilters;
    this.catchExceptionClassFilter = catchExceptionClassFilter;
  }

  @Override
  public MethodVisitor create(MethodVisitor methodVisitor) {
    MethodVisitor visitor;
    visitor = new SEMethodVisitor(methodVisitor);
    visitor = new SEClassFilterMethodVisitor(visitor, this.classFilters, this.catchExceptionClassFilter);
    return visitor;
  }

}
