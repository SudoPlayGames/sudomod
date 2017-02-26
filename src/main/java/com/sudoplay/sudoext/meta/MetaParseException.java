package com.sudoplay.sudoext.meta;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class MetaParseException extends
    Exception {

  public MetaParseException() {
  }

  public MetaParseException(String message) {
    super(message);
  }

  public MetaParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public MetaParseException(Throwable cause) {
    super(cause);
  }

  public MetaParseException(String message, Throwable cause, boolean enableSuppression, boolean
      writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
