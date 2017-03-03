package com.sudoplay.sudoext.container;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This cache maintains a LRU cache for all plugins loaded in a container.
 * <p>
 * Created by codetaylor on 2/22/2017.
 */
public class LRUContainerCache implements
    IContainerCache {

  private Map<Class<?>, Object> cache;

  public LRUContainerCache(final int maxEntries) {
    this.cache = new LinkedHashMap<Class<?>, Object>(maxEntries, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxEntries;
      }
    };
  }

  @Override
  public <T> void put(Class<T> tClass, T object) {
    this.cache.put(tClass, object);
  }

  @Override
  public <T> T get(Class<T> tClass) {
    return tClass.cast(this.cache.get(tClass));
  }
}
