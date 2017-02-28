package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class InstructionLimitException extends
    RuntimeException {

  public InstructionLimitException() {
  }

  public InstructionLimitException(String message) {
    super(message);
  }

  public InstructionLimitException(String message, Throwable cause) {
    super(message, cause);
  }

  public InstructionLimitException(Throwable cause) {
    super(cause);
  }

  public InstructionLimitException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
