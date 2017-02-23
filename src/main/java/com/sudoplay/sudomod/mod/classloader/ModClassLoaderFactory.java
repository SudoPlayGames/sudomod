package com.sudoplay.sudomod.mod.classloader;

import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.security.FilteredClassLoader;
import com.sudoplay.sudomod.mod.security.IClassFilter;
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
public class ModClassLoaderFactory {

  private final URL[] urls;
  private File[] sourcePath;
  private List<ModContainer> modDependencyList;
  private IClassFilter[] classFilters;

  public ModClassLoaderFactory(
      Path path,
      List<String> jarFileList,
      List<ModContainer> modDependencyList,
      IClassFilter[] classFilters
  ) {
    this.modDependencyList = modDependencyList;
    this.classFilters = classFilters;
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

  public ModSourceClassLoader create() {

    FilteredClassLoader filteredClassLoader = new FilteredClassLoader(
        this.getClass().getClassLoader(),
        this.classFilters
    );

    ModJarClassLoader modJarClassLoader = new ModJarClassLoader(
        this.urls,
        filteredClassLoader
    );

    ModDependencyClassLoader modDependencyClassLoader = new ModDependencyClassLoader(
        modJarClassLoader,
        this.modDependencyList
    );

    ModSourceClassLoader modSourceClassLoader = new ModSourceClassLoader(
        modDependencyClassLoader,
        new JavaSourceIClassLoader(
            new PathResourceFinder(this.sourcePath),
            null,
            new ClassLoaderIClassLoader(modDependencyClassLoader)
        )
    );

    modSourceClassLoader.setDebuggingInfo(true, true, true);

    return modSourceClassLoader;
  }

}
