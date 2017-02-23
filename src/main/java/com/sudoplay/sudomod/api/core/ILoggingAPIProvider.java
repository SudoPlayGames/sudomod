package com.sudoplay.sudomod.api.core;

import com.sudoplay.sudomod.api.LoggingAPI;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface ILoggingAPIProvider {
  LoggingAPI getLoggingAPI(Class<?> aClass);
}
