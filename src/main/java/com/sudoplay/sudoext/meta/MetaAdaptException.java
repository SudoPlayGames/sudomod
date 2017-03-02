package com.sudoplay.sudoext.meta;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class MetaAdaptException extends
    Exception {

  public MetaAdaptException() {
  }

  public MetaAdaptException(String message) {
    super(message);
  }

  public MetaAdaptException(String message, Throwable cause) {
    super(message, cause);
  }

  public MetaAdaptException(Throwable cause) {
    super(cause);
  }

  public MetaAdaptException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
