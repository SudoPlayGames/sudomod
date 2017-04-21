package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoxt.classloader.filter.IClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.intercept.IClassInterceptor;
import com.sudoplay.sudoxt.classloader.intercept.InterceptClassLoader;
import com.sudoplay.sudoxt.container.Container;
import com.sudoplay.sudoxt.util.InputStreamByteArrayConverter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/21/2017.
 */
/* package */ class ClassLoaderFactory implements
    IClassLoaderFactory {

  private static final boolean DEBUG = true;

  private final URL[] urls;
  private File[] sourcePath;
  private final Path path;
  private List<Container> dependencyList;
  private IClassFilterPredicate classFilterPredicate;
  private IClassInterceptor classInterceptor;
  private IByteCodeTransformer byteCodeTransformer;
  private InputStreamByteArrayConverter inputStreamByteArrayConverter;
  private ICompilerFactory compilerFactory;

  /* package */ ClassLoaderFactory(
      Path path,
      Set<String> jarFileSet,
      List<Container> dependencyList,
      IClassFilterPredicate classFilterPredicate,
      IClassInterceptor classInterceptor,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter,
      ICompilerFactory compilerFactory
  ) {
    this.path = path;
    this.dependencyList = dependencyList;
    this.classFilterPredicate = classFilterPredicate;
    this.classInterceptor = classInterceptor;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
    this.compilerFactory = compilerFactory;
    int size = jarFileSet.size();
    this.urls = new URL[size];

    int i = 0;
    for (String jarFile : jarFileSet) {

      try {
        this.urls[i] = path.resolve(jarFile).toUri().toURL();
        i += 1;

      } catch (MalformedURLException e) {
        // this should be prevented by the URL conversion check in the validation phase
      }
    }

    this.sourcePath = new File[]{path.toFile()};
  }

  @Override
  public IContainerClassLoader create() {

    InterceptClassLoader interceptClassLoader = new InterceptClassLoader(
        this.path,
        this.getClass().getClassLoader(),
        this.classInterceptor
    );

    ICompiler compiler = this.compilerFactory.create(DEBUG);

    SXClassLoader classLoader = new SXClassLoader(
        this.path,
        this.urls,
        interceptClassLoader,
        this.dependencyList,
        this.byteCodeTransformer,
        this.inputStreamByteArrayConverter,
        this.classFilterPredicate,
        compiler,
        true
    );

    compiler.onClassLoaderInit(classLoader, this.sourcePath);

    return classLoader;
  }
}
