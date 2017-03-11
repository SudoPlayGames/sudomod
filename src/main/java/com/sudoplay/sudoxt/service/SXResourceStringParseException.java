package com.sudoplay.sudoxt.service;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class SXResourceStringParseException extends
    Exception {

  public SXResourceStringParseException() {
  }

  public SXResourceStringParseException(String message) {
    super(message);
  }

  public SXResourceStringParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public SXResourceStringParseException(Throwable cause) {
    super(cause);
  }

  public SXResourceStringParseException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
