package com.sudoplay.sudoxt.service;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ResourceStringParseException extends
    Exception {

  public ResourceStringParseException() {
  }

  public ResourceStringParseException(String message) {
    super(message);
  }

  public ResourceStringParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceStringParseException(Throwable cause) {
    super(cause);
  }

  public ResourceStringParseException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
