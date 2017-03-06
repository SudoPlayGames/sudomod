package com.sudoplay.sudoext.classloader.asm.transform;

import com.sudoplay.sudoext.classloader.asm.filter.AllowedJavaLangClassFilter;
import com.sudoplay.sudoext.classloader.asm.filter.AllowedPrimitivesClassFilter;
import com.sudoplay.sudoext.classloader.asm.filter.RestrictedTryCatchExceptionClassFilter;
import com.sudoplay.sudoext.classloader.filter.ClassFilterPredicate;
import com.sudoplay.sudoext.classloader.filter.IClassFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class SEByteCodeTransformerBuilder {

  private IClassReaderFactory classReaderFactory;
  private IClassWriterFactory classWriterFactory;
  private IClassReaderAcceptor classReaderAcceptor;
  private IByteCodePrinter byteCodePrinter;

  private List<IClassFilter> defaultClassFilterList;
  private List<IClassFilter> defaultCatchExceptionClassFilterList;

  private List<IClassFilter> classFilterList;
  private List<IClassFilter> catchExceptionClassFilterList;

  public SEByteCodeTransformerBuilder() {
    this.classFilterList = new ArrayList<>();
    this.catchExceptionClassFilterList = new ArrayList<>();

    this.classReaderFactory = new DefaultClassReaderFactory();
    this.classWriterFactory = new DefaultClassWriterFactory();
    this.classReaderAcceptor = new DefaultClassReaderAcceptor();
    this.byteCodePrinter = new NoOpByteCodePrinter();

    this.defaultClassFilterList = new ArrayList<>();
    this.defaultClassFilterList.addAll(Arrays.asList(
        new AllowedPrimitivesClassFilter(),
        new AllowedJavaLangClassFilter()
    ));

    this.defaultCatchExceptionClassFilterList = new ArrayList<>();
    this.defaultCatchExceptionClassFilterList.addAll(Arrays.asList(
        new RestrictedTryCatchExceptionClassFilter()
    ));
  }

  public SEByteCodeTransformerBuilder setClassReaderFactory(IClassReaderFactory factory) {
    this.classReaderFactory = factory;
    return this;
  }

  public SEByteCodeTransformerBuilder setClassWriterFactory(IClassWriterFactory factory) {
    this.classWriterFactory = factory;
    return this;
  }

  public SEByteCodeTransformerBuilder setClassReaderAcceptor(IClassReaderAcceptor acceptor) {
    this.classReaderAcceptor = acceptor;
    return this;
  }

  public SEByteCodeTransformerBuilder setByteCodePrinter(IByteCodePrinter printer) {
    this.byteCodePrinter = printer;
    return this;
  }

  public SEByteCodeTransformerBuilder addClassFilter(IClassFilter filter) {
    this.classFilterList.add(filter);
    return this;
  }

  public SEByteCodeTransformerBuilder removeAllDefaultClassFilters() {
    this.defaultClassFilterList.clear();
    return this;
  }

  public SEByteCodeTransformerBuilder removeDefaultClassFilter(Class<? extends IClassFilter> aClass) {
    this.removeByClass(aClass, this.defaultClassFilterList);
    return this;
  }

  public SEByteCodeTransformerBuilder addCatchExceptionClassFilter(IClassFilter filter) {
    this.catchExceptionClassFilterList.add(filter);
    return this;
  }

  public SEByteCodeTransformerBuilder removeAllDefaultCatchExceptionClassFilters() {
    this.defaultCatchExceptionClassFilterList.clear();
    return this;
  }

  public SEByteCodeTransformerBuilder removeDefaultCatchExceptionClassFilter(Class<? extends IClassFilter> aClass) {
    this.removeByClass(aClass, this.defaultCatchExceptionClassFilterList);
    return this;
  }

  private IClassFilter[] getClassFilters() {
    List<IClassFilter> list = new ArrayList<>();
    list.addAll(this.defaultClassFilterList);
    list.addAll(this.classFilterList);
    return list.toArray(new IClassFilter[list.size()]);
  }

  private IClassFilter[] getCatchExceptionClassFilters() {
    List<IClassFilter> list = new ArrayList<>();
    list.addAll(this.defaultCatchExceptionClassFilterList);
    list.addAll(this.catchExceptionClassFilterList);
    return list.toArray(new IClassFilter[list.size()]);
  }

  private IClassReaderFactory getClassReaderFactory() {
    return classReaderFactory;
  }

  private IClassWriterFactory getClassWriterFactory() {
    return classWriterFactory;
  }

  private IClassReaderAcceptor getClassReaderAcceptor() {
    return classReaderAcceptor;
  }

  private IByteCodePrinter getByteCodePrinter() {
    return byteCodePrinter;
  }

  public SEByteCodeTransformer create() {

    return new SEByteCodeTransformer(
        this.getClassReaderFactory(),
        new SEClassVisitorFactory(
            new SEMethodVisitorFactory(
                new ClassFilterPredicate(
                    this.getClassFilters()
                ),
                new ClassFilterPredicate(
                    this.getCatchExceptionClassFilters()
                )
            )
        ),
        this.getClassWriterFactory(),
        this.getClassReaderAcceptor(),
        this.getByteCodePrinter()
    );
  }

  private SEByteCodeTransformerBuilder removeByClass(Class<?> aClass, List<?> list) {

    for (Iterator<?> it = list.iterator(); it.hasNext(); ) {

      if (aClass.isAssignableFrom(it.next().getClass())) {
        it.remove();
      }
    }
    return this;
  }
}
