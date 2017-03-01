package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.intercept.IClassInterceptor;
import com.sudoplay.sudoext.classloader.intercept.InterceptClassLoader;
import com.sudoplay.sudoext.container.Container;
import org.codehaus.janino.ClassLoaderIClassLoader;
import org.codehaus.janino.JavaSourceIClassLoader;
import org.codehaus.janino.util.resource.PathResourceFinder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/21/2017.
 */
/* package */ class ClassLoaderFactory implements
    IClassLoaderFactory {

  private final URL[] urls;
  private File[] sourcePath;
  private List<Container> dependencyList;
  private IClassFilter[] classFilters;
  private IClassInterceptor classInterceptor;
  private IByteCodeTransformer byteCodeTransformer;

  /* package */ ClassLoaderFactory(
      Path path,
      List<String> jarFileList,
      List<Container> dependencyList,
      IClassFilter[] classFilters,
      IClassInterceptor classInterceptor,
      IByteCodeTransformer byteCodeTransformer
  ) {
    this.dependencyList = dependencyList;
    this.classFilters = classFilters;
    this.classInterceptor = classInterceptor;
    this.byteCodeTransformer = byteCodeTransformer;
    int size = jarFileList.size();
    this.urls = new URL[size];

    for (int i = 0; i < size; i++) {

      try {
        this.urls[i] = path.resolve(jarFileList.get(i)).toUri().toURL();

      } catch (MalformedURLException e) {
        // this should be prevented by the URL conversion check in the validation phase
      }
    }

    this.sourcePath = new File[]{path.toFile()};
  }

  @Override
  public SourceClassLoader create() {

    FilteredClassLoader filteredClassLoader = new FilteredClassLoader(
        this.getClass().getClassLoader(),
        this.classFilters
    );

    InterceptClassLoader interceptClassLoader = new InterceptClassLoader(
        filteredClassLoader,
        this.classInterceptor
    );

    JarClassLoader jarClassLoader = new JarClassLoader(
        this.urls,
        interceptClassLoader,
        this.byteCodeTransformer
    );

    DependencyClassLoader dependencyClassLoader = new DependencyClassLoader(
        jarClassLoader,
        this.dependencyList
    );

    SourceClassLoader sourceClassLoader = new SourceClassLoader(
        dependencyClassLoader,
        new JavaSourceIClassLoader(
            new PathResourceFinder(this.sourcePath),
            null,
            new ClassLoaderIClassLoader(dependencyClassLoader)
        ),
        this.byteCodeTransformer
    );

    sourceClassLoader.setDebuggingInfo(true, true, true);

    return sourceClassLoader;
  }

}
