package com.sudoplay.sudoxt.api;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface ILoggerAPIProvider {

  LoggerAPI getLoggerAPI(Class<?> aClass);
}
