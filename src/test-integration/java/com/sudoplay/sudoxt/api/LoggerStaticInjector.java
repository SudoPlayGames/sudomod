package com.sudoplay.sudoxt.api;

import com.sudoplay.sudoxt.classloader.intercept.StaticInjector;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class LoggerStaticInjector extends
    StaticInjector<ILoggerAPIProvider> {

  public LoggerStaticInjector() {
    super(
        ILoggerAPIProvider.class,
        container -> new LoggerAPIProvider(container.getId())
    );
  }
}
