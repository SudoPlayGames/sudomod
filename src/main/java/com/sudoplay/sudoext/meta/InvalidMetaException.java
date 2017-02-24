package com.sudoplay.sudoext.meta;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class InvalidMetaException extends
    Exception {

  public InvalidMetaException() {
  }

  public InvalidMetaException(String message) {
    super(message);
  }

  public InvalidMetaException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidMetaException(Throwable cause) {
    super(cause);
  }

  public InvalidMetaException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
