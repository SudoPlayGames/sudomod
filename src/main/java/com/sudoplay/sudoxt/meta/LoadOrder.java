package com.sudoplay.sudoxt.meta;

import java.util.Arrays;

/**
 * Created by codetaylor on 2/18/2017.
 */
public enum LoadOrder {

  Before("before"),
  After("after"),
  RequiredBefore("required-before"),
  RequiredAfter("required-after");

  private String key;

  LoadOrder(String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }

  public static LoadOrder from(String key) {
    LoadOrder[] values = LoadOrder.values();

    for (LoadOrder value : values) {

      if (value.key.equals(key)) {
        return value;
      }
    }

    String[] acceptedValues = new String[values.length];

    for (int i = 0; i < values.length; i++) {
      acceptedValues[i] = values[i].getKey();
    }

    throw new IllegalArgumentException(String.format("Unrecognized load order key: %s - accepted values are %s", key,
        Arrays.toString(acceptedValues)));
  }
}
