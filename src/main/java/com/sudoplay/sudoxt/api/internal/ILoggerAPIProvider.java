package com.sudoplay.sudoxt.api.internal;

import com.sudoplay.sudoxt.api.common.LoggerAPI;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface ILoggerAPIProvider {

  LoggerAPI getLoggerAPI(Class<?> aClass);
}
