package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.meta.IMetaListProvider;
import com.sudoplay.sudoext.meta.Meta;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class ContainerMapCreatorTest {

  @Test
  public void getContainerMapReturnsContainerMap() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    metaA.setId("metaA");
    metaB.setId("metaB");

    IMetaListProvider listProvider = mock(IMetaListProvider.class);
    when(listProvider.getMetaList()).thenReturn(new ArrayList<>(Arrays.asList(
        metaA,
        metaB
    )));

    ContainerMapCreator containerMapCreator = new ContainerMapCreator(listProvider, null, null);

    Map<String, Container> containerMap = containerMapCreator.getContainerMap();

    Assert.assertEquals("metaA", containerMap.get("metaA").getId());
    Assert.assertEquals("metaB", containerMap.get("metaB").getId());
  }

}