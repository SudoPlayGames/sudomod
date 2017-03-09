package com.sudoplay.sudoxt.api;

import testapi.LoggerAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class LoggerAPIProvider implements
    ILoggerAPIProvider {

  private String id;

  public LoggerAPIProvider(String id) {
    this.id = id;
  }

  @Override
  public LoggerAPI getLoggerAPI(Class<?> aClass) {
    String name = this.id + ":" + aClass.getName();
    Logger logger = LoggerFactory.getLogger(name);
    return new LoggerProxy(logger);
  }
}
