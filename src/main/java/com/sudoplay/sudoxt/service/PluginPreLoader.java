package com.sudoplay.sudoxt.service;

import java.util.List;

/**
 * Created by codetaylor on 3/6/2017.
 */
/* package */ class PluginPreLoader implements
    IPluginPreloader {

  @Override
  public void preload(
      IPreloadMonitor monitor,
      List<SXResourceLocation> preloadList,
      IPluginProvider service
  ) {

    int listSize = preloadList.size();

    for (int i = 0; i < listSize; i++) {
      SXResourceLocation resourceLocation;
      Throwable throwable;
      long start;

      resourceLocation = preloadList.get(i);
      throwable = null;
      start = System.currentTimeMillis();

      try {
        service.getPlugin(resourceLocation, Object.class).preLoad();

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
