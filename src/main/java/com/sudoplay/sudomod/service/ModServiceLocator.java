package com.sudoplay.sudomod.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModServiceLocator {

  private static final Map<String, ModService> MOD_SERVICE_MAP;

  static {
    MOD_SERVICE_MAP = new HashMap<>();
  }

  public static void registerModService(String id, ModService modService) {

    if (MOD_SERVICE_MAP.containsKey(id)) {
      throw new IllegalArgumentException(String.format("Mod service id [%s] already registered to [%s]", id,
          MOD_SERVICE_MAP.get(id)));
    }

    MOD_SERVICE_MAP.put(id, modService);
  }

  public static ModService locate(String id) {
    ModService modService = MOD_SERVICE_MAP.get(id);

    if (modService == null) {
      throw new IllegalArgumentException(String.format("No mod service registered for id [%s]", id));
    }

    return modService;
  }

}
