package com.sudoplay.sudoext.api.internal;

import com.sudoplay.sudoext.api.external.LogAPI;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface ILogAPIProvider {

  LogAPI getLoggerAPI(Class<?> aClass);
}
