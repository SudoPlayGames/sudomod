package com.sudoplay.sudoxt;

import com.sudoplay.sudoxt.api.AncillaryPlugin;
import com.sudoplay.sudoxt.api.ILoggerAPIProvider;
import com.sudoplay.sudoxt.api.LoggerAPI;
import com.sudoplay.sudoxt.api.Plugin;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class WhitelistClassFilter implements
    IClassFilter {

  private Set<String> whitelist;

  public WhitelistClassFilter() {

    this.whitelist = new HashSet<>();

    // java.lang interfaces
    this.whitelist.addAll(Arrays.asList(
        java.lang.Appendable.class.getName(),
        java.lang.CharSequence.class.getName(),
        java.lang.Cloneable.class.getName(),
        java.lang.Comparable.class.getName(),
        java.lang.Iterable.class.getName()
    ));

    // java.lang classes
    this.whitelist.addAll(Arrays.asList(
        "java.lang.AbstractStringBuilder",
        java.lang.Boolean.class.getName(),
        java.lang.Byte.class.getName(),
        java.lang.Character.class.getName(),
        java.lang.Class.class.getName(),
        java.lang.Double.class.getName(),
        java.lang.Enum.class.getName(),
        java.lang.Float.class.getName(),
        java.lang.Integer.class.getName(),
        java.lang.Long.class.getName(),
        java.lang.Math.class.getName(),
        java.lang.Number.class.getName(),
        java.lang.Object.class.getName(),
        java.lang.Short.class.getName(),
        java.lang.String.class.getName(),
        java.lang.StringBuffer.class.getName(),
        java.lang.StringBuilder.class.getName(),
        java.lang.Throwable.class.getName()
    ));

    // java.lang exceptions
    this.whitelist.addAll(Arrays.asList(
        ArithmeticException.class.getName(),
        ArrayIndexOutOfBoundsException.class.getName(),
        ArrayStoreException.class.getName(),
        ClassCastException.class.getName(),
        ClassNotFoundException.class.getName(),
        CloneNotSupportedException.class.getName(),
        EnumConstantNotPresentException.class.getName(),
        Exception.class.getName(),
        IllegalAccessException.class.getName(),
        IllegalArgumentException.class.getName(),
        IllegalMonitorStateException.class.getName(),
        IllegalStateException.class.getName(),
        IllegalThreadStateException.class.getName(),
        IndexOutOfBoundsException.class.getName(),
        InstantiationException.class.getName(),
        InterruptedException.class.getName(),
        NegativeArraySizeException.class.getName(),
        NoSuchFieldException.class.getName(),
        NoSuchMethodException.class.getName(),
        NullPointerException.class.getName(),
        NumberFormatException.class.getName(),
        ReflectiveOperationException.class.getName(),
        RuntimeException.class.getName(),
        SecurityException.class.getName(),
        StringIndexOutOfBoundsException.class.getName(),
        TypeNotPresentException.class.getName(),
        UnsupportedOperationException.class.getName()
    ));

    // java.lang errors
    this.whitelist.addAll(Arrays.asList(
        AbstractMethodError.class.getName(),
        AssertionError.class.getName(),
        BootstrapMethodError.class.getName(),
        ClassCircularityError.class.getName(),
        ClassFormatError.class.getName(),
        Error.class.getName(),
        ExceptionInInitializerError.class.getName(),
        IllegalAccessError.class.getName(),
        IncompatibleClassChangeError.class.getName(),
        InstantiationError.class.getName(),
        InternalError.class.getName(),
        LinkageError.class.getName(),
        NoClassDefFoundError.class.getName(),
        NoSuchFieldError.class.getName(),
        NoSuchMethodError.class.getName(),
        OutOfMemoryError.class.getName(),
        StackOverflowError.class.getName(),
        ThreadDeath.class.getName(),
        UnknownError.class.getName(),
        UnsatisfiedLinkError.class.getName(),
        UnsupportedClassVersionError.class.getName(),
        VerifyError.class.getName(),
        VirtualMachineError.class.getName()
    ));

    // java.lang annotations
    this.whitelist.addAll(Arrays.asList(
        Deprecated.class.getName(),
        Override.class.getName(),
        Retention.class.getName(),
        RetentionPolicy.class.getName(),
        SafeVarargs.class.getName(),
        SuppressWarnings.class.getName(),
        Target.class.getName()
    ));

    // java.io
    this.whitelist.addAll(Arrays.asList(
        Serializable.class.getName()
    ));

    // java.util
    this.whitelist.addAll(Arrays.asList(
        Iterator.class.getName()
    ));

    // com.sudoplay.sudoext
    this.whitelist.addAll(Arrays.asList(
        ILoggerAPIProvider.class.getName(),
        LoggerAPI.class.getName()
    ));

    // ugh
    this.whitelist.addAll(Arrays.asList(
        Plugin.class.getName(),
        AncillaryPlugin.class.getName(),

        // not happy about allowing these
        System.class.getName(),
        ClassLoader.class.getName(),

        // seem to be required, maybe for annotation processing
        java.lang.reflect.AnnotatedElement.class.getName(),
        java.lang.reflect.GenericDeclaration.class.getName(),
        java.lang.reflect.Type.class.getName()
    ));
  }

  @Override
  public boolean isAllowed(String name) {
    return this.whitelist.contains(name);
  }
}
