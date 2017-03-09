package com.sudoplay.sudoxt.service;

import java.util.List;

/**
 * Created by codetaylor on 3/6/2017.
 */
/* package */ class PluginPreLoader {

  /* package */ void preload(
      IPreloadMonitor monitor,
      List<ResourceLocation> preloadList,
      SEService service
  ) {

    int listSize = preloadList.size();

    for (int i = 0; i < listSize; i++) {
      ResourceLocation resourceLocation;
      Throwable throwable;
      long start;

      resourceLocation = preloadList.get(i);
      throwable = null;
      start = System.currentTimeMillis();

      try {
        service.getPlugin(resourceLocation, Plugin.class).preLoad();

      } catch (Exception e) {
        throwable = e;
      }

      monitor.update(
          resourceLocation.getId(),
          resourceLocation.getResource(),
          (i + 1) / (float) listSize,
          System.currentTimeMillis() - start,
          throwable
      );
    }
  }
}
