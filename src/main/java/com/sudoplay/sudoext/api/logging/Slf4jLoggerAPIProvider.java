package com.sudoplay.sudoext.api.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Slf4jLoggerAPIProvider implements
    ILoggerAPIProvider {

  private String id;

  public Slf4jLoggerAPIProvider(String id) {
    this.id = id;
  }

  @Override
  public LoggerAPI getLoggerAPI(Class<?> aClass) {
    String name = this.id + ":" + aClass.getName();
    Logger logger = LoggerFactory.getLogger(name);
    return new Slf4JLoggerAPI(logger);
  }
}
