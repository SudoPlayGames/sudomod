package com.sudoplay.sudoxt.classloader.asm.transform;

import com.sudoplay.sudoxt.classloader.asm.ClassAllocation;
import com.sudoplay.sudoxt.classloader.asm.exception.RestrictedUseException;
import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class SEClassVisitor extends
    ClassVisitor {

  private IMethodVisitorFactory methodVisitorFactory;
  private final IClassFilterPredicate classFilterPredicate;

  private String name;

  public SEClassVisitor(
      ClassVisitor classVisitor,
      IMethodVisitorFactory methodVisitorFactory,
      IClassFilterPredicate classFilterPredicate
  ) {
    super(ASM5, classVisitor);
    this.methodVisitorFactory = methodVisitorFactory;
    this.classFilterPredicate = classFilterPredicate;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    this.name = name;

    if (!this.classFilterPredicate.isAllowed(superName)) {
      throw new RestrictedUseException(String.format(
          "Usage of class [%s] is prohibited",
          superName
      ));
    }

    for (String iface : interfaces) {

      if (!this.classFilterPredicate.isAllowed(iface)) {
        throw new RestrictedUseException(String.format(
            "Usage of class [%s] is prohibited",
            iface
        ));
      }
    }

    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
    ClassAllocation.increment(this.name, 1);
    return super.visitField(access, name, desc, signature, value);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

    MethodVisitor methodVisitor;

    if ("finalize".equals(name)) {
      throw new RestrictedUseException("Usage of the finalize() method is prohibited in extensions");
    }

    methodVisitor = this.cv.visitMethod(access, name, desc, signature, exceptions);

    if (methodVisitor != null) {
      return this.methodVisitorFactory.create(methodVisitor, access, name, desc, signature, exceptions);
    }

    return null;
  }
}
