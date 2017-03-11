package com.sudoplay.sudoxt.service;

/**
 * Created by codetaylor on 3/5/2017.
 */
public class SXPluginException extends
    Exception {

  public SXPluginException() {
  }

  public SXPluginException(String message) {
    super(message);
  }

  public SXPluginException(String message, Throwable cause) {
    super(message, cause);
  }

  public SXPluginException(Throwable cause) {
    super(cause);
  }

  public SXPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
