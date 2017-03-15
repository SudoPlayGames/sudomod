package com.sudoplay.sudoxt.service;

import java.util.List;

/**
 * Created by codetaylor on 3/15/2017.
 */
public interface IPluginPreloader {
  void preload(
      IPreloadMonitor monitor,
      List<SXResourceLocation> preloadList,
      IPluginProvider service
  );
}
