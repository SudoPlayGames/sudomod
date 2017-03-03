package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.meta.Dependency;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class DependencyContainerListMapperTest {

  @Test
  public void getDependencyContainerListShouldReturnContainerList() throws Exception {

    Container containerA = new Container(null, null, null, this.callbackDelegateFactory.create());
    Container containerB = new Container(null, null, null, this.callbackDelegateFactory.create());
    Container containerC = new Container(null, null, null, this.callbackDelegateFactory.create());

    Map<String, Container> containerMap = new HashMap<>();

    containerMap.put("a", containerA);
    containerMap.put("b", containerB);
    containerMap.put("c", containerC);

    List<Dependency> dependencyList = new ArrayList<>(Arrays.asList(
        new Dependency("a", null, null),
        new Dependency("b", null, null)
    ));

    List<Container> result = new DependencyContainerListMapper()
        .getDependencyContainerList(containerMap, dependencyList);

    Assert.assertTrue(result.contains(containerA));
    Assert.assertTrue(result.contains(containerB));
    Assert.assertFalse(result.contains(containerC));
  }

}