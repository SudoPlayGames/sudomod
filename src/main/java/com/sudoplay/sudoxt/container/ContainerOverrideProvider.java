package com.sudoplay.sudoxt.container;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class ContainerOverrideProvider {

  private Map<String, ContainerOverride> containerMap;

  public ContainerOverrideProvider(Map<String, ContainerOverride> containerMap) {
    this.containerMap = containerMap;
  }

  @Nullable
  public <P> P getPlugin(String resource, Class<P> pClass) throws
      ClassNotFoundException, IllegalAccessException, InstantiationException {

    ContainerOverride containerOverride = this.containerMap.get(resource);

    if (containerOverride != null) {
      return containerOverride.getContainer().getPlugin(containerOverride.getResource(), pClass);
    }

    return null;
  }
}
