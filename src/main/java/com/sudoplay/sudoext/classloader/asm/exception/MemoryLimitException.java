package com.sudoplay.sudoext.classloader.asm.exception;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class MemoryLimitException extends
    RuntimeException {

  public MemoryLimitException() {
  }

  public MemoryLimitException(String message) {
    super(message);
  }

  public MemoryLimitException(String message, Throwable cause) {
    super(message, cause);
  }

  public MemoryLimitException(Throwable cause) {
    super(cause);
  }

  public MemoryLimitException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
