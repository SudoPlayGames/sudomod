package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoext.meta.IMetaListProvider;
import com.sudoplay.sudoext.meta.Meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class ContainerMapCreator implements
    IContainerMapProvider {

  private IMetaListProvider metaListProvider;
  private IContainerCacheFactory containerCacheFactory;
  private ICallbackDelegateFactory callbackDelegateFactory;
  private PluginInstantiator pluginInstantiator;

  public ContainerMapCreator(
      IMetaListProvider metaListProvider,
      IContainerCacheFactory containerCacheFactory,
      PluginInstantiator pluginInstantiator
  ) {
    this.metaListProvider = metaListProvider;
    this.containerCacheFactory = containerCacheFactory;
    this.pluginInstantiator = pluginInstantiator;
  }

  @Override
  public Map<String, Container> getContainerMap() {

    Map<String, Container> containerMap;

    containerMap = new HashMap<>();

    // create containers
    for (Meta meta : this.metaListProvider.getMetaList()) {
      String id = meta.getId();
      Container container = new Container(
          id,
          this.containerCacheFactory,
          this.pluginInstantiator,
          this.callbackDelegateFactory.create()
      );
      containerMap.put(id, container);
    }

    return Collections.unmodifiableMap(containerMap);
  }
}
