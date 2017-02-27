package com.sudoplay.sudoext.candidate;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class CandidateProcessorException extends
    Exception {

  public CandidateProcessorException() {
  }

  public CandidateProcessorException(String message) {
    super(message);
  }

  public CandidateProcessorException(String message, Throwable cause) {
    super(message, cause);
  }

  public CandidateProcessorException(Throwable cause) {
    super(cause);
  }

  public CandidateProcessorException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
