package com.sudoplay.sudoext.api;

import com.sudoplay.sudoext.api.logging.ILoggerAPIProvider;
import com.sudoplay.sudoext.api.logging.LoggerAPI;
import com.sudoplay.sudoext.meta.Meta;

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
  private static Meta META;

  public static String getId() {
    return META.getId();
  }

  private ContainerAPI() {
    //
  }
}
