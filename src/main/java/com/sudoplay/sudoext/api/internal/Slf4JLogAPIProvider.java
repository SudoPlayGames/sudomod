package com.sudoplay.sudoext.api.internal;

import com.sudoplay.sudoext.api.external.LogAPI;
import com.sudoplay.sudoext.api.internal.proxy.Slf4JLogProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Slf4JLogAPIProvider implements
    ILogAPIProvider {

  private String id;

  public Slf4JLogAPIProvider(String id) {
    this.id = id;
  }

  @Override
  public LogAPI getLoggerAPI(Class<?> aClass) {
    String name = this.id + ":" + aClass.getName();
    Logger logger = LoggerFactory.getLogger(name);
    return new Slf4JLogProxy(logger);
  }
}
