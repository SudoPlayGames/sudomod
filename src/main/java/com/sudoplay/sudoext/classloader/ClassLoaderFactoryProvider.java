package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.filter.IClassFilterPredicate;
import com.sudoplay.sudoext.classloader.intercept.IClassInterceptorFactory;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;

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

  public ClassLoaderFactoryProvider(
      IClassFilterPredicate filteredClassLoaderPredicate,
      IClassInterceptorFactory classInterceptorFactory,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter
  ) {
    this.filteredClassLoaderPredicate = filteredClassLoaderPredicate;
    this.classInterceptorFactory = classInterceptorFactory;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
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
        this.inputStreamByteArrayConverter
    );
  }
}
