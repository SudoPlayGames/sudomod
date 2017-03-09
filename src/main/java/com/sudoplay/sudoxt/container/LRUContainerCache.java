package com.sudoplay.sudoxt.container;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This cache maintains a LRU cache for all plugins loaded in a container.
 * <p>
 * Created by codetaylor on 2/22/2017.
 */
public class LRUContainerCache implements
    IContainerCache {

  private Map<String, Object> cache;

  public LRUContainerCache(final int maxEntries) {
    this.cache = new LinkedHashMap<String, Object>(maxEntries, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxEntries;
      }
    };
  }

  @Override
  public <T> void put(String resourceString, T object) {
    this.cache.put(resourceString, object);
  }

  @Override
  public <T> T get(String resourceString) {
    return (T) this.cache.get(resourceString);
  }
}
