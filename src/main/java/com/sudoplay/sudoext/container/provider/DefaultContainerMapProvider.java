package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.container.Container;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class DefaultContainerMapProvider implements
    IContainerMapProvider {

  private IContainerListProvider containerListProvider;
  private Supplier<Map<String, Container>> mapSupplier;

  public DefaultContainerMapProvider(
      IContainerListProvider containerListProvider,
      Supplier<Map<String, Container>> mapSupplier
  ) {
    this.containerListProvider = containerListProvider;
    this.mapSupplier = mapSupplier;
  }

  @Override
  public Map<String, Container> getContainerMap() {

    Map<String, Container> map = this.mapSupplier.get();

    for (Container container : this.containerListProvider.getContainerList()) {
      map.put(container.getMeta().getId(), container);
    }

    return Collections.unmodifiableMap(map);
  }
}
