package com.sudoplay.sudoext.container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class LRUContainerCacheFactory implements
    IContainerCacheFactory {

  private int maxSize;

  public LRUContainerCacheFactory(int maxSize) {
    this.maxSize = maxSize;
  }

  @Override
  public IContainerCache create() {
    return new LRUContainerCache(this.maxSize);
  }
}
