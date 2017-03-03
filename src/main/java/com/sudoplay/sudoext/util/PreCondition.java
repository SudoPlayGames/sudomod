package com.sudoplay.sudoext.util;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class PreCondition {

  public static <T> T notNull(T object) {

    if (object == null) {
      throw new NullPointerException();
    }

    return object;
  }

  private PreCondition() {
    //
  }
}
