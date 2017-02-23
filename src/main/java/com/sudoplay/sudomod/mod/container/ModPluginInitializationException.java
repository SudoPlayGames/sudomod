package com.sudoplay.sudomod.mod.container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class ModPluginInitializationException extends
    RuntimeException {

  public ModPluginInitializationException() {
  }

  public ModPluginInitializationException(String message) {
    super(message);
  }

  public ModPluginInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ModPluginInitializationException(Throwable cause) {
    super(cause);
  }

  public ModPluginInitializationException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
