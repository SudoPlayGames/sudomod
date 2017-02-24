package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.intercept.IClassInterceptorFactory;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.security.IClassFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class ClassLoaderFactoryProvider implements
    IClassLoaderFactoryProvider {

  private IClassFilter[] classFilters;
  private IClassInterceptorFactory classInterceptorFactory;

  public ClassLoaderFactoryProvider(
      IClassFilter[] classFilters,
      IClassInterceptorFactory classInterceptorFactory
  ) {
    this.classFilters = classFilters;
    this.classInterceptorFactory = classInterceptorFactory;
  }

  @Override
  public IClassLoaderFactory create(
      Container container,
      Map<String, Container> containerMap
  ) {
    Meta meta = container.getMeta();

    // build the dependency list
    List<Container> dependencyList = new ArrayList<>();

    for (Dependency dependency : meta.getDependencyContainer().getDependencyList()) {
      String dependencyId = dependency.getId();
      Container dependencyContainer = containerMap.get(dependencyId);

      if (dependencyContainer != null) {
        dependencyList.add(dependencyContainer);
      }
    }

    return new ClassLoaderFactory(
        container.getPath(),
        meta.getJarFileList(),
        dependencyList,
        this.classFilters,
        this.classInterceptorFactory.create(container)
    );
  }
}
