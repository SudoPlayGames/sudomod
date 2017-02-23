package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.api.core.ILoggingAPIProvider;
import com.sudoplay.sudomod.api.LoggingAPI;

/**
 * Created by codetaylor on 2/21/2017.
 */
public abstract class ModPlugin {

  private static ILoggingAPIProvider LOGGING_API_PROVIDER;

  /* package */ void initialize(ILoggingAPIProvider ILoggingAPIProvider) {
    ModPlugin.LOGGING_API_PROVIDER = ILoggingAPIProvider;
    this.onPluginInitialized();
  }

  protected abstract void onPluginInitialized();

  @SuppressWarnings("WeakerAccess")
  public static LoggingAPI getLoggingAPI(Class<?> aClass) {
    return ModPlugin.LOGGING_API_PROVIDER.getLoggingAPI(aClass);
  }
}
