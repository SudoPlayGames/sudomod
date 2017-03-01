package com.sudoplay.sudoext.classloader.asm.exception;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class ArrayDimensionLimitException extends
    RuntimeException {

  public ArrayDimensionLimitException() {
  }

  public ArrayDimensionLimitException(String message) {
    super(message);
  }

  public ArrayDimensionLimitException(String message, Throwable cause) {
    super(message, cause);
  }

  public ArrayDimensionLimitException(Throwable cause) {
    super(cause);
  }

  public ArrayDimensionLimitException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
