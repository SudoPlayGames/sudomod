package com.sudoplay.sudoxt.container;

import com.sudoplay.sudoxt.meta.IMetaListProvider;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.service.SXResourceLocation;
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

    Map<String, Map<String, ContainerOverride>> overrideMap;
    List<Meta> metaList;
    Map<String, Container> containerMap;

    overrideMap = new HashMap<>();
    metaList = this.metaListProvider.getMetaList();
    containerMap = this.containerMapProvider.getContainerMap();

    for (Meta meta : metaList) {

      for (Map.Entry<SXResourceLocation, SXResourceLocation> entry : meta.getOverrideMap().entrySet()) {
        SXResourceLocation remoteResourceLocation;
        String remoteContainerId;

        remoteResourceLocation = entry.getKey();
        remoteContainerId = remoteResourceLocation.getId();

        // if the container to override exists...
        if (containerMap.containsKey(remoteContainerId)) {
          SXResourceLocation localResourceLocation;
          String localResource;
          String remoteResource;
          String localContainerId;
          Map<String, ContainerOverride> map;
          Container localContainer;

          localResourceLocation = entry.getValue();

          localResource = localResourceLocation.getResource();
          remoteResource = remoteResourceLocation.getResource();

          localContainerId = localResourceLocation.getId();
          map = this.getSafe(overrideMap, remoteContainerId);
          localContainer = containerMap.get(localContainerId);
          map.put(remoteResource, new ContainerOverride(localResource, localContainer));
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
  private Map<String, ContainerOverride> getSafe(
      Map<String, Map<String, ContainerOverride>> overrideMap,
      String remoteContainerId
  ) {
    Map<String, ContainerOverride> map = overrideMap.get(remoteContainerId);

    if (map == null) {
      map = new HashMap<>();
      overrideMap.put(remoteContainerId, map);
    }
    return map;
  }
}
