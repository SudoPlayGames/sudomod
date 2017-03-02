package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.filter.IClassFilterPredicate;
import com.sudoplay.sudoext.classloader.intercept.IClassInterceptor;
import com.sudoplay.sudoext.classloader.intercept.InterceptClassLoader;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;
import org.codehaus.janino.ClassLoaderIClassLoader;
import org.codehaus.janino.JavaSourceIClassLoader;
import org.codehaus.janino.util.resource.PathResourceFinder;

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

  private final URL[] urls;
  private File[] sourcePath;
  private List<Container> dependencyList;
  private IClassFilterPredicate classFilterPredicate;
  private IClassInterceptor classInterceptor;
  private IByteCodeTransformer byteCodeTransformer;
  private InputStreamByteArrayConverter inputStreamByteArrayConverter;

  /* package */ ClassLoaderFactory(
      Path path,
      Set<String> jarFileSet,
      List<Container> dependencyList,
      IClassFilterPredicate classFilterPredicate,
      IClassInterceptor classInterceptor,
      IByteCodeTransformer byteCodeTransformer,
      InputStreamByteArrayConverter inputStreamByteArrayConverter
  ) {
    this.dependencyList = dependencyList;
    this.classFilterPredicate = classFilterPredicate;
    this.classInterceptor = classInterceptor;
    this.byteCodeTransformer = byteCodeTransformer;
    this.inputStreamByteArrayConverter = inputStreamByteArrayConverter;
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
  public SourceClassLoader create() {

    FilteredClassLoader filteredClassLoader = new FilteredClassLoader(
        this.getClass().getClassLoader(),
        this.classFilterPredicate
    );

    InterceptClassLoader interceptClassLoader = new InterceptClassLoader(
        filteredClassLoader,
        this.classInterceptor
    );

    JarClassLoader jarClassLoader = new JarClassLoader(
        this.urls,
        interceptClassLoader,
        this.byteCodeTransformer,
        this.inputStreamByteArrayConverter
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
