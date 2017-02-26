package com.sudoplay.sudoext.util;

import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class CloseUtil {

  public static void close(Closeable closeable, Logger logger) {

    if (closeable == null) {
      return;
    }

    try {
      closeable.close();

    } catch (IOException e) {
      logger.error("Error closing closeable", e);
    }
  }

  private CloseUtil() {
    //
  }
}
