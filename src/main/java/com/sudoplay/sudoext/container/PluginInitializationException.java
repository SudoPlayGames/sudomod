package com.sudoplay.sudoext.container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class PluginInitializationException extends
    RuntimeException {

  public PluginInitializationException() {
  }

  public PluginInitializationException(String message) {
    super(message);
  }

  public PluginInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public PluginInitializationException(Throwable cause) {
    super(cause);
  }

  public PluginInitializationException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
