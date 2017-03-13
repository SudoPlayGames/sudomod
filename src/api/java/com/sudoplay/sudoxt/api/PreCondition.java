package com.sudoplay.sudoxt.api;

/**
 * Created by codetaylor on 3/12/2017.
 */
public class PreCondition {

  public static <O> O notNull(O o) {

    if (o == null) {
      throw new NullPointerException();
    }
    return o;
  }

  public static int notNegative(int i) {

    if (i < 0) {
      throw new IllegalArgumentException(String.format("Expected non-negative number, got %d", i));
    }
    return i;
  }

  private PreCondition() {
    //
  }
}
