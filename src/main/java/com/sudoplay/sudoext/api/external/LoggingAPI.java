package com.sudoplay.sudoext.api.external;

import com.sudoplay.sudoext.api.internal.ILogAPIProvider;
import com.sudoplay.sudoext.classloader.intercept.InjectStaticField;
import com.sudoplay.sudoext.classloader.intercept.InterceptClass;

/**
 * Created by codetaylor on 3/7/2017.
 */
@InterceptClass
public class LoggingAPI {

  @InjectStaticField
  private static ILogAPIProvider LOG_API_PROVIDER;

  public static LogAPI getLog(Class<?> aClass) {
    return LOG_API_PROVIDER.getLoggerAPI(aClass);
  }

  private LoggingAPI() {
    //
  }
}
