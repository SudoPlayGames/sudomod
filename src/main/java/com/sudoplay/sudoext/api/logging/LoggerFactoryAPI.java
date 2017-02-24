package com.sudoplay.sudoext.api.logging;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class LoggerFactoryAPI {

  // injected
  @SuppressWarnings("unused")
  private static ILoggerAPIProvider LOGGING_API_PROVIDER;

  public static LoggerAPI getLoggerAPI(Class<?> aClass) {
    return LOGGING_API_PROVIDER.getLoggerAPI(aClass);
  }

  private LoggerFactoryAPI() {
    //
  }
}
