package com.sudoplay.sudoxt.classloader.asm.exception;

/**
 * Created by codetaylor on 3/5/2017.
 */
public class ConstantStringPoolLimitException extends
    RuntimeException {

  public ConstantStringPoolLimitException() {
  }

  public ConstantStringPoolLimitException(String message) {
    super(message);
  }

  public ConstantStringPoolLimitException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConstantStringPoolLimitException(Throwable cause) {
    super(cause);
  }

  public ConstantStringPoolLimitException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
