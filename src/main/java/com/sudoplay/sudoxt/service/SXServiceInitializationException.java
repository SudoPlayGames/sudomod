package com.sudoplay.sudoxt.service;

/**
 * This should only be thrown if a fatal error occurs while initializing the service.
 * <p>
 * A fatal error being any error that prevents the service from functioning properly.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class SXServiceInitializationException extends
    Exception {

  public SXServiceInitializationException() {
  }

  public SXServiceInitializationException(String message) {
    super(message);
  }

  public SXServiceInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SXServiceInitializationException(Throwable cause) {
    super(cause);
  }

  public SXServiceInitializationException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
