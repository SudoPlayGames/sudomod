package com.sudoplay.sudoxt.container;

import com.sudoplay.sudoxt.meta.IMetaListProvider;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.service.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class OverrideContainerMapProvider implements
    IContainerMapProvider {

  private IContainerMapProvider containerMapProvider;
  private IMetaListProvider metaListProvider;

  public OverrideContainerMapProvider(
      IMetaListProvider metaListProvider, IContainerMapProvider containerMapProvider
  ) {
    this.containerMapProvider = containerMapProvider;
    this.metaListProvider = metaListProvider;
  }

  @Override
  public Map<String, Container> getContainerMap() {

    Map<String, Map<String, Container>> overrideMap;
    List<Meta> metaList;
    Map<String, Container> containerMap;

    overrideMap = new HashMap<>();
    metaList = this.metaListProvider.getMetaList();
    containerMap = this.containerMapProvider.getContainerMap();

    for (Meta meta : metaList) {

      for (Map.Entry<ResourceLocation, ResourceLocation> entry : meta.getOverrideMap().entrySet()) {
        String remoteContainerId = entry.getKey().getId();

        // if the container to override exists...
        if (containerMap.containsKey(remoteContainerId)) {
          ResourceLocation localResourceLocation;
          String localResource;
          String localContainerId;
          Map<String, Container> map;
          Container localContainer;

          localResourceLocation = entry.getValue();
          localResource = localResourceLocation.getResource();
          localContainerId = localResourceLocation.getId();
          map = this.getSafe(overrideMap, remoteContainerId);
          localContainer = containerMap.get(localContainerId);
          map.put(localResource, localContainer);
        }
      }
    }

    for (Container container : containerMap.values()) {
      container.setContainerOverrideProvider(new ContainerOverrideProvider(
          this.getSafe(overrideMap, container.getId())
      ));
    }

    return containerMap;
  }

  @NotNull
  private Map<String, Container> getSafe(
      Map<String, Map<String, Container>> overrideMap,
      String remoteContainerId
  ) {
    Map<String, Container> map = overrideMap.get(remoteContainerId);

    if (map == null) {
      map = new HashMap<>();
      overrideMap.put(remoteContainerId, map);
    }
    return map;
  }
}
