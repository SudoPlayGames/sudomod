package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class ArraySizeLimitException extends
    RuntimeException {

  public ArraySizeLimitException() {
  }

  public ArraySizeLimitException(String message) {
    super(message);
  }

  public ArraySizeLimitException(String message, Throwable cause) {
    super(message, cause);
  }

  public ArraySizeLimitException(Throwable cause) {
    super(cause);
  }

  public ArraySizeLimitException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
