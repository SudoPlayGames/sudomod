package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.container.Container;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/23/2017.
 */
public interface IClassLoaderFactoryProvider {

  IClassLoaderFactory create(
      String id,
      Path path,
      Set<String> jarFileSet,
      List<Container> dependencyList);
}
