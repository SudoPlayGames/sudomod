package com.sudoplay.sudoxt.classloader.asm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 3/5/2017.
 */
public class ClassAllocation {

  // injected
  private static final Map<String, Integer> CLASS_FIELD_COUNT_MAP = new HashMap<>();

  public static int get(String name) {
    Integer count = CLASS_FIELD_COUNT_MAP.get(name);

    if (count == null) {
      return -1;
    }

    return count;
  }

  public static void put(String name, int count) {
    CLASS_FIELD_COUNT_MAP.put(name, count);
  }

  public static void increment(String name, int increment) {
    Integer count = CLASS_FIELD_COUNT_MAP.get(name);

    if (count == null) {
      count = 0;
    }

    CLASS_FIELD_COUNT_MAP.put(name, count + increment);
  }

}
