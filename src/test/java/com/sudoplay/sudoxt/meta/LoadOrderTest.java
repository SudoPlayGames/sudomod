package com.sudoplay.sudoxt.meta;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class LoadOrderTest {

  @Test
  public void fromShouldReturnLoadOrderEnum() throws Exception {

    for (LoadOrder loadOrder : LoadOrder.values()) {
      Assert.assertEquals(loadOrder, LoadOrder.from(loadOrder.getKey()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromShouldThrowWhenInvalidKey() {
    LoadOrder.from("invalid-key");
  }
}
