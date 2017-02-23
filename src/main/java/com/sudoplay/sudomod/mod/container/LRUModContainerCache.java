package com.sudoplay.sudomod.mod.container;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This cache maintains a hard reference to the ModPlugin and a LRU cache for all other mod objects loaded.
 * <p>
 * Created by codetaylor on 2/22/2017.
 */
public class LRUModContainerCache implements
    IModContainerCache {

  private ModPlugin modPluginCache;
  private Map<Class<?>, Object> cache;

  public LRUModContainerCache(final int maxEntries) {
    this.cache = new LinkedHashMap<Class<?>, Object>(maxEntries, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxEntries;
      }
    };
  }

  @Override
  public <T> void put(Class<T> tClass, T object) {

    if (this.modPluginCache == null && ModPlugin.class.isAssignableFrom(tClass)) {
      this.modPluginCache = (ModPlugin) object;

    } else {
      this.cache.put(tClass, object);
    }
  }

  @Override
  public <T> T get(Class<T> tClass) {

    if (ModPlugin.class.isAssignableFrom(tClass)) {
      return tClass.cast(this.modPluginCache);

    } else {
      return tClass.cast(this.cache.get(tClass));
    }
  }
}
