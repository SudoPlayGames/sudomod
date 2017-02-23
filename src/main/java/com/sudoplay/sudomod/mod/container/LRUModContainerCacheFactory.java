package com.sudoplay.sudomod.mod.container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class LRUModContainerCacheFactory implements
    IModContainerCacheFactory {

  private int maxSize;

  public LRUModContainerCacheFactory(int maxSize) {
    this.maxSize = maxSize;
  }

  @Override
  public IModContainerCache create() {
    return new LRUModContainerCache(this.maxSize);
  }
}
