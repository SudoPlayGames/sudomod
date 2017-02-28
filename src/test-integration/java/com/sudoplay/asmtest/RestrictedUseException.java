package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class RestrictedUseException extends
    RuntimeException {

  public RestrictedUseException() {
  }

  public RestrictedUseException(String message) {
    super(message);
  }

  public RestrictedUseException(String message, Throwable cause) {
    super(message, cause);
  }

  public RestrictedUseException(Throwable cause) {
    super(cause);
  }

  public RestrictedUseException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
