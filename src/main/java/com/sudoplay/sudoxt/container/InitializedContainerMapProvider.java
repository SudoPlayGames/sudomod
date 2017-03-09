package com.sudoplay.sudoxt.container;

import com.sudoplay.sudoxt.classloader.IClassLoaderFactoryProvider;
import com.sudoplay.sudoxt.meta.IMetaListProvider;
import com.sudoplay.sudoxt.meta.Meta;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class InitializedContainerMapProvider implements
    IContainerMapProvider {

  private IMetaListProvider metaListProvider;
  private IContainerMapProvider containerMapProvider;
  private IClassLoaderFactoryProvider classLoaderFactoryProvider;
  private DependencyContainerListMapper dependencyContainerListMapper;

  public InitializedContainerMapProvider(
      IMetaListProvider metaListProvider,
      IContainerMapProvider containerMapProvider,
      IClassLoaderFactoryProvider classLoaderFactoryProvider,
      DependencyContainerListMapper dependencyContainerListMapper
  ) {
    this.metaListProvider = metaListProvider;
    this.containerMapProvider = containerMapProvider;
    this.classLoaderFactoryProvider = classLoaderFactoryProvider;
    this.dependencyContainerListMapper = dependencyContainerListMapper;
  }

  @Override
  public Map<String, Container> getContainerMap() {
    Map<String, Container> containerMap;

    containerMap = this.containerMapProvider.getContainerMap();

    for (Meta meta : this.metaListProvider.getMetaList()) {
      Container container = containerMap.get(meta.getId());

      // set the class loader factory
      container.setClassLoaderFactory(this.classLoaderFactoryProvider.create(
          container,
          meta.getParentPath(),
          meta.getJarFileSet(),
          this.dependencyContainerListMapper.getDependencyContainerList(
              containerMap,
              meta.getAllDependencies(new ArrayList<>())
          )
      ));
    }

    return containerMap;
  }
}
