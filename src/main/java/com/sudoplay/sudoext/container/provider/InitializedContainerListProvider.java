package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.classloader.IClassLoaderFactoryProvider;
import com.sudoplay.sudoext.container.Container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class InitializedContainerListProvider implements
    IContainerListProvider {

  private IContainerListProvider containerListProvider;
  private IClassLoaderFactoryProvider classLoaderFactoryProvider;

  public InitializedContainerListProvider(
      IContainerListProvider containerListProvider,
      IClassLoaderFactoryProvider classLoaderFactoryProvider
  ) {
    this.containerListProvider = containerListProvider;
    this.classLoaderFactoryProvider = classLoaderFactoryProvider;
  }

  @Override
  public List<Container> getContainerList() {
    List<Container> containerList;
    Map<String, Container> containerMap;

    containerList = this.containerListProvider
        .getContainerList();

    containerMap = new HashMap<>();

    for (Container container : containerList) {
      containerMap.put(container.getMeta().getId(), container);
    }

    // initialize containers
    for (Container container : containerList) {

      // init the container's class loader factory
      container.setClassLoaderFactory(
          this.classLoaderFactoryProvider.create(
              container,
              containerMap
          )
      );
    }

    return containerList;
  }
}
