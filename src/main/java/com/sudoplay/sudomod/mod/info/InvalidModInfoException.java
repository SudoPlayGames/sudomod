package com.sudoplay.sudomod.mod.info;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class InvalidModInfoException extends
    Exception {

  public InvalidModInfoException() {
  }

  public InvalidModInfoException(String message) {
    super(message);
  }

  public InvalidModInfoException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidModInfoException(Throwable cause) {
    super(cause);
  }

  public InvalidModInfoException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
