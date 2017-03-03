package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.classloader.IClassLoaderFactoryProvider;
import com.sudoplay.sudoext.meta.IMetaListProvider;
import com.sudoplay.sudoext.meta.Meta;

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
    String id;

    containerMap = this.containerMapProvider.getContainerMap();

    for (Meta meta : this.metaListProvider.getMetaList()) {
      id = meta.getId();

      // set the class loader factory
      containerMap.get(id).setClassLoaderFactory(this.classLoaderFactoryProvider.create(
          id,
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
