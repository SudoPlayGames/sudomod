package com.sudoplay.sudoxt.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class SEServiceRegistry {

  private static final Map<String, SEService> SERVICE_MAP;

  static {
    SERVICE_MAP = new HashMap<>();
  }

  public static void registerService(String id, SEService service) {

    if (SERVICE_MAP.containsKey(id)) {
      throw new IllegalArgumentException(String.format(
          "Service id [%s] already registered to [%s]",
          id,
          SERVICE_MAP.get(id)
      ));
    }

    SERVICE_MAP.put(id, service);
  }

  public static SEService get(String id) {
    SEService service = SERVICE_MAP.get(id);

    if (service == null) {
      throw new IllegalArgumentException(String.format("No service registered for id [%s]", id));
    }

    return service;
  }

}
