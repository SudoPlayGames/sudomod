package com.sudoplay.sudoxt.container;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class ContainerOverrideProvider {

  private Map<String, Container> containerMap;

  public ContainerOverrideProvider(Map<String, Container> containerMap) {
    this.containerMap = containerMap;
  }

  @Nullable
  public <P> P getPlugin(String resource, Class<P> pClass) throws
      ClassNotFoundException, IllegalAccessException, InstantiationException {

    Container container = this.containerMap.get(resource);

    if (container != null) {
      return container.getPlugin(resource, pClass);
    }

    return null;
  }
}
