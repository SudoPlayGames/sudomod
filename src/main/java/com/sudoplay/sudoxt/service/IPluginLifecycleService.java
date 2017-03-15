package com.sudoplay.sudoxt.service;

/**
 * Created by codetaylor on 3/15/2017.
 */
public interface IPluginLifecycleService {
  void preload(IPreloadMonitor monitor);

  void reload();

  void dispose();
}
