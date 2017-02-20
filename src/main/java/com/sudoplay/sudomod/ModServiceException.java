package com.sudoplay.sudomod;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModServiceException extends
    Exception {

  public ModServiceException() {
  }

  public ModServiceException(String message) {
    super(message);
  }

  public ModServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ModServiceException(Throwable cause) {
    super(cause);
  }

  public ModServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
