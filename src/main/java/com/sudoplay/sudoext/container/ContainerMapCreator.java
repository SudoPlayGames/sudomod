package com.sudoplay.sudoext.container;

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
  private IContainerFactory containerFactory;


  public ContainerMapCreator(
      IMetaListProvider metaListProvider,
      IContainerFactory containerFactory
  ) {
    this.metaListProvider = metaListProvider;
    this.containerFactory = containerFactory;
  }

  @Override
  public Map<String, Container> getContainerMap() {

    Map<String, Container> containerMap;

    containerMap = new HashMap<>();

    // create containers
    for (Meta meta : this.metaListProvider.getMetaList()) {
      String id = meta.getId();
      containerMap.put(id, this.containerFactory.create(id, meta.getRegisteredPluginMap()));
    }

    return Collections.unmodifiableMap(containerMap);
  }
}
