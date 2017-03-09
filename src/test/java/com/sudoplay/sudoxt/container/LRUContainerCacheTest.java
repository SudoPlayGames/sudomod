package com.sudoplay.sudoxt.container;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class LRUContainerCacheTest {

  private class TestA {
    //
  }

  private class TestB {
    //
  }

  @Test
  public void cacheShouldExpire() throws Exception {

    LRUContainerCache cache = new LRUContainerCache(1);

    TestA testA = new TestA();
    TestB testB = new TestB();

    cache.put("TestA", testA);

    Assert.assertTrue(testA == cache.get("TestA"));

    cache.put("TestB", testB);

    Assert.assertTrue(testB == cache.get("TestB"));
    Assert.assertTrue(null == cache.get("TestA"));
  }

}