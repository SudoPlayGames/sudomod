package com.sudoplay.sudoext.util;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class PathUtil {

  public static String clean(String path) {
    path = path.replaceAll("\\\\", "/");
    path = path.replaceAll("/{2,}", "/");
    path = path.replaceAll("[\\.]{2,}", "");
    path = path.replaceAll("[^a-z0-9-_/\\.]", "");

    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    return path;
  }

  private PathUtil() {
    //
  }
}
