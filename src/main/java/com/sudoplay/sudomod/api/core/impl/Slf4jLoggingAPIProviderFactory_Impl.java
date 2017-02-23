package com.sudoplay.sudomod.api.core.impl;

import com.sudoplay.sudomod.api.core.ILoggingAPIProvider;
import com.sudoplay.sudomod.api.core.ILoggingAPIProviderFactory;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Slf4jLoggingAPIProviderFactory_Impl implements
    ILoggingAPIProviderFactory {

  @Override
  public ILoggingAPIProvider create(String modId) {
    return new Slf4jLoggingAPIProvider_Impl(modId);
  }
}
