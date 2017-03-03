package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.classloader.IClassLoaderFactoryProvider;
import com.sudoplay.sudoext.meta.IMetaListProvider;
import com.sudoplay.sudoext.meta.Meta;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class InitializedContainerMapProviderTest {

  @Test
  public void getContainerMap() throws Exception {

    Container containerA = mock(Container.class);
    Container containerB = mock(Container.class);
    Container containerC = mock(Container.class);

    Map<String, Container> containerMap = new HashMap<>();
    containerMap.put("a", containerA);
    containerMap.put("b", containerB);
    containerMap.put("c", containerC);

    IContainerMapProvider containerMapProvider = mock(IContainerMapProvider.class);
    when(containerMapProvider.getContainerMap()).thenReturn(containerMap);

    Meta metaA = mock(Meta.class);
    Meta metaB = mock(Meta.class);
    Meta metaC = mock(Meta.class);

    when(metaA.getId()).thenReturn("a");
    when(metaB.getId()).thenReturn("b");
    when(metaC.getId()).thenReturn("c");

    List<Meta> metaList = new ArrayList<>();
    metaList.add(metaA);
    metaList.add(metaB);
    metaList.add(metaC);

    IMetaListProvider metaListProvider = mock(IMetaListProvider.class);
    when(metaListProvider.getMetaList()).thenReturn(metaList);

    IClassLoaderFactoryProvider classLoaderFactoryProvider = mock(IClassLoaderFactoryProvider.class);
    DependencyContainerListMapper dependencyContainerListMapper = mock(DependencyContainerListMapper.class);

    InitializedContainerMapProvider provider = new InitializedContainerMapProvider(
        metaListProvider,
        containerMapProvider,
        classLoaderFactoryProvider,
        dependencyContainerListMapper
    );

    provider.getContainerMap();

    verify(containerA, times(1)).setClassLoaderFactory(any());
    verify(containerB, times(1)).setClassLoaderFactory(any());
    verify(containerC, times(1)).setClassLoaderFactory(any());

    verify(classLoaderFactoryProvider, times(3)).create(any(), any(), any(), any());
  }

}