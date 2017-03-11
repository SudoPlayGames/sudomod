package com.sudoplay.sudoxt.api;

import com.sudoplay.sudoxt.api.ILoggerAPIProvider;
import com.sudoplay.sudoxt.classloader.intercept.InjectStaticField;
import com.sudoplay.sudoxt.classloader.intercept.InterceptClass;

/**
 * Created by codetaylor on 3/7/2017.
 */
@InterceptClass
public class LoggingAPI {

  @InjectStaticField
  private static ILoggerAPIProvider LOG_API_PROVIDER;

  public static LoggerAPI getLog(Class<?> aClass) {
    return LOG_API_PROVIDER.getLoggerAPI(aClass);
  }

  private LoggingAPI() {
    //
  }
}
