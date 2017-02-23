package com.sudoplay.sudomod.api.core;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface ILoggingAPIProviderFactory {

  ILoggingAPIProvider create(String modId);
}
