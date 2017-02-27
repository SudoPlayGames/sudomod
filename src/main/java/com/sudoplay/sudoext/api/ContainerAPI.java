package com.sudoplay.sudoext.api;

import com.sudoplay.sudoext.api.logging.ILoggerAPIProvider;
import com.sudoplay.sudoext.api.logging.LoggerAPI;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class ContainerAPI {

  // injected
  @SuppressWarnings("unused")
  private static ILoggerAPIProvider LOGGING_API_PROVIDER;

  public static LoggerAPI getLoggerAPI(Class<?> aClass) {
    return LOGGING_API_PROVIDER.getLoggerAPI(aClass);
  }

  // injected
  @SuppressWarnings("unused")
  private static String ID;

  public static String getId() {
    return ID;
  }

  private ContainerAPI() {
    //
  }
}
