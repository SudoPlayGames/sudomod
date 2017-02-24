package com.sudoplay.sudoext.container;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class ContainerFactory implements
    IContainerFactory {

  private IContainerCacheFactory containerCacheFactory;

  public ContainerFactory(
      IContainerCacheFactory containerCacheFactory
  ) {
    this.containerCacheFactory = containerCacheFactory;
  }

  @Override
  public Container create(
      Path path
  ) {
    return new Container(
        path,
        this.containerCacheFactory
    );
  }
}
