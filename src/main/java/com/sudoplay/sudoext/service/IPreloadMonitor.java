package com.sudoplay.sudoext.service;

/**
 * Created by codetaylor on 3/6/2017.
 */
public interface IPreloadMonitor {

  void update(String containerId, String resource, float percentage, long timeMilliseconds, Throwable throwable);
}
