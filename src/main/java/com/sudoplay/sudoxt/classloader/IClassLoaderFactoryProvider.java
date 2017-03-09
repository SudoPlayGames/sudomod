package com.sudoplay.sudoxt.classloader;

import com.sudoplay.sudoxt.container.Container;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/23/2017.
 */
public interface IClassLoaderFactoryProvider {

  IClassLoaderFactory create(
      Container container,
      Path path,
      Set<String> jarFileSet,
      List<Container> dependencyList);
}
