package com.sudoplay.sudoext.container;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

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

    cache.put(TestA.class, testA);

    Assert.assertTrue(testA == cache.get(TestA.class));

    cache.put(TestB.class, testB);

    Assert.assertTrue(testB == cache.get(TestB.class));
    Assert.assertTrue(null == cache.get(TestA.class));
  }

}