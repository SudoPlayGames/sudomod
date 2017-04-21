package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.intercept.IClassInterceptorFactory;
import com.sudoplay.sudoxt.container.Container;
import com.sudoplay.sudoxt.util.InputStreamByteArrayConverter;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class ClassLoaderFactoryProvider implements
    IClassLoaderFactoryProvider {

  private IClassFilterPredicate filteredClassLoaderPredicate;
  private IClassInterceptorFactory classInterceptorFactory;
  private IByteCodeTransformer byteCodeTransformer;
  private InputStreamByteArrayConverter inputStreamByteArrayConverter;
  private ICompilerFactory compilerFactory;

  public ClassLoaderFactoryProvider(
      IClassFilterPredicate filteredClassLoaderPredicate,
      IClassInterceptorFactory classInterceptorFactory,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter,
      ICompilerFactory compilerFactory
  ) {
    this.filteredClassLoaderPredicate = filteredClassLoaderPredicate;
    this.classInterceptorFactory = classInterceptorFactory;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
    this.compilerFactory = compilerFactory;
  }

  @Override
  public IClassLoaderFactory create(
      Container container,
      Path path,
      Set<String> jarFileSet,
      List<Container> dependencyList
  ) {
    return new ClassLoaderFactory(
        path,
        jarFileSet,
        dependencyList,
        this.filteredClassLoaderPredicate,
        this.classInterceptorFactory.create(container),
        this.byteCodeTransformer,
        this.inputStreamByteArrayConverter,
        this.compilerFactory
    );
  }
}
