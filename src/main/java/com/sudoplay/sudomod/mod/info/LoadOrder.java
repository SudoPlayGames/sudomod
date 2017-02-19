package com.sudoplay.sudomod.mod.info;

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

    for (LoadOrder value : LoadOrder.values()) {

      if (value.key.equals(key)) {
        return value;
      }
    }
    throw new IllegalArgumentException(String.format("Unrecognized load order key: %s", key));
  }
}
