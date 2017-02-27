package com.sudoplay.asmtest;

import com.sudoplay.sudoext.security.IClassFilter;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEMethodVisitorFactory implements IMethodVisitorFactory {

  private IClassFilter[] classFilters;

  public SEMethodVisitorFactory(IClassFilter[] classFilters) {
    this.classFilters = classFilters;
  }

  @Override
  public MethodVisitor create(MethodVisitor methodVisitor) {
    return new SEMethodVisitor(methodVisitor, this.classFilters);
  }

}
