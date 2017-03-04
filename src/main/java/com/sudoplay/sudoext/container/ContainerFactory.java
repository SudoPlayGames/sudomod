package com.sudoplay.sudoext.container;

import java.util.Map;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class ContainerFactory implements IContainerFactory {

  private IContainerCacheFactory containerCacheFactory;
  private PluginInstantiator pluginInstantiator;

  public ContainerFactory(
      IContainerCacheFactory containerCacheFactory,
      PluginInstantiator pluginInstantiator
  ) {
    this.containerCacheFactory = containerCacheFactory;
    this.pluginInstantiator = pluginInstantiator;
  }

  @Override
  public Container create(
      String id,
      Map<String, String> registeredPluginMap
  ) {
    return new Container(
        id,
        this.containerCacheFactory,
        this.pluginInstantiator,
        registeredPluginMap
    );
  }

}
