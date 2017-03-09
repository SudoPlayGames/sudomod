package com.sudoplay.sudoxt.classloader.asm.exception;

/**
 * Created by codetaylor on 2/28/2017.
 */
public class TimeLimitException extends
    RuntimeException {

  public TimeLimitException() {
  }

  public TimeLimitException(String message) {
    super(message);
  }

  public TimeLimitException(String message, Throwable cause) {
    super(message, cause);
  }

  public TimeLimitException(Throwable cause) {
    super(cause);
  }

  public TimeLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
