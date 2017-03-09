package com.sudoplay.sudoxt.sort;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class CyclicGraphException extends
    RuntimeException {

  public CyclicGraphException() {
  }

  public CyclicGraphException(String message) {
    super(message);
  }

  public CyclicGraphException(String message, Throwable cause) {
    super(message, cause);
  }

  public CyclicGraphException(Throwable cause) {
    super(cause);
  }

  public CyclicGraphException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
