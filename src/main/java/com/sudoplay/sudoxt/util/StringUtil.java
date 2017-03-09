package com.sudoplay.sudoxt.util;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class StringUtil {

  public static int countOccurrences(String string, char c) {
    int count = 0;

    for (int i = 0; i < string.length(); i++) {

      if (string.charAt(i) == c) {
        count += 1;
      }
    }
    return count;
  }

  private StringUtil() {
    //
  }
}
