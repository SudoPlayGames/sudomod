package com.sudoplay.sudoxt.classloader.asm.transform;

import com.sudoplay.sudoxt.classloader.asm.filter.ASMClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.asm.filter.AllowedJavaLangClassFilter;
import com.sudoplay.sudoxt.classloader.asm.filter.AllowedPrimitivesClassFilter;
import com.sudoplay.sudoxt.classloader.asm.filter.RestrictedTryCatchExceptionClassFilter;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class SXByteCodeTransformerBuilder {

  private IClassReaderFactory classReaderFactory;
  private IClassWriterFactory classWriterFactory;
  private IClassReaderAcceptor classReaderAcceptor;
  private IByteCodePrinter byteCodePrinter;

  private List<IClassFilter> defaultClassFilterList;
  private List<IClassFilter> defaultCatchExceptionClassFilterList;

  private List<IClassFilter> classFilterList;
  private List<IClassFilter> catchExceptionClassFilterList;

  public SXByteCodeTransformerBuilder() {
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

  public SXByteCodeTransformerBuilder setClassReaderFactory(IClassReaderFactory factory) {
    this.classReaderFactory = factory;
    return this;
  }

  public SXByteCodeTransformerBuilder setClassWriterFactory(IClassWriterFactory factory) {
    this.classWriterFactory = factory;
    return this;
  }

  public SXByteCodeTransformerBuilder setClassReaderAcceptor(IClassReaderAcceptor acceptor) {
    this.classReaderAcceptor = acceptor;
    return this;
  }

  public SXByteCodeTransformerBuilder setByteCodePrinter(IByteCodePrinter printer) {
    this.byteCodePrinter = printer;
    return this;
  }

  public SXByteCodeTransformerBuilder addClassFilter(IClassFilter filter) {
    this.classFilterList.add(filter);
    return this;
  }

  public SXByteCodeTransformerBuilder removeAllDefaultClassFilters() {
    this.defaultClassFilterList.clear();
    return this;
  }

  public SXByteCodeTransformerBuilder removeDefaultClassFilter(Class<? extends IClassFilter> aClass) {
    this.removeByClass(aClass, this.defaultClassFilterList);
    return this;
  }

  public SXByteCodeTransformerBuilder addCatchExceptionClassFilter(IClassFilter filter) {
    this.catchExceptionClassFilterList.add(filter);
    return this;
  }

  public SXByteCodeTransformerBuilder removeAllDefaultCatchExceptionClassFilters() {
    this.defaultCatchExceptionClassFilterList.clear();
    return this;
  }

  public SXByteCodeTransformerBuilder removeDefaultCatchExceptionClassFilter(Class<? extends IClassFilter> aClass) {
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
                new ASMClassFilterPredicate(
                    this.getClassFilters()
                ),
                new ASMClassFilterPredicate(
                    this.getCatchExceptionClassFilters()
                )
            ),
            new ASMClassFilterPredicate(
                this.getClassFilters()
            )
        ),
        this.getClassWriterFactory(),
        this.getClassReaderAcceptor(),
        this.getByteCodePrinter()
    );
  }

  private SXByteCodeTransformerBuilder removeByClass(Class<?> aClass, List<?> list) {

    for (Iterator<?> it = list.iterator(); it.hasNext(); ) {

      if (aClass.isAssignableFrom(it.next().getClass())) {
        it.remove();
      }
    }
    return this;
  }
}
