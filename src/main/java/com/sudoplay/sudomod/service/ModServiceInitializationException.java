package com.sudoplay.sudomod.service;

/**
 * This should only be thrown if a fatal error occurs while initializing the mod service.
 * <p>
 * A fatal error being any error that prevents the mod service from functioning properly.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class ModServiceInitializationException extends
    Exception {

  public ModServiceInitializationException() {
  }

  public ModServiceInitializationException(String message) {
    super(message);
  }

  public ModServiceInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ModServiceInitializationException(Throwable cause) {
    super(cause);
  }

  public ModServiceInitializationException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
