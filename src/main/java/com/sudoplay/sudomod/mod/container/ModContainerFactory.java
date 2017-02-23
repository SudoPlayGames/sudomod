package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.api.core.ILoggingAPIProviderFactory;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class ModContainerFactory implements
    IModContainerFactory {

  private IModContainerCacheFactory modContainerCacheFactory;
  private ILoggingAPIProviderFactory loggingAPIProviderFactory;

  public ModContainerFactory(
      IModContainerCacheFactory modContainerCacheFactory,
      ILoggingAPIProviderFactory loggingAPIProviderFactory
  ) {
    this.modContainerCacheFactory = modContainerCacheFactory;
    this.loggingAPIProviderFactory = loggingAPIProviderFactory;
  }

  @Override
  public ModContainer create(
      Path modPath
  ) {
    return new ModContainer(
        modPath,
        this.modContainerCacheFactory,
        this.loggingAPIProviderFactory
    );
  }
}
