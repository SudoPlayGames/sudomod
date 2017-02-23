package com.sudoplay.sudomod.api.core.impl;

import com.sudoplay.sudomod.api.LoggingAPI;
import com.sudoplay.sudomod.api.core.ILoggingAPIProvider;
import com.sudoplay.sudomod.api.impl.Slf4jLoggingAPI_Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Slf4jLoggingAPIProvider_Impl implements
    ILoggingAPIProvider {

  private String modId;

  public Slf4jLoggingAPIProvider_Impl(String modId) {
    this.modId = modId;
  }

  @Override
  public LoggingAPI getLoggingAPI(Class<?> aClass) {
    String name = this.modId + ":" + aClass.getName();
    Logger logger = LoggerFactory.getLogger(name);
    return new Slf4jLoggingAPI_Impl(logger);
  }
}
