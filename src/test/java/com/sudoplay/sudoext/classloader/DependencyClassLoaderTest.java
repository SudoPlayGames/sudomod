package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.container.Container;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class DependencyClassLoaderTest {

  @Test
  public void loadClassInvokesDependencyContainerClassLoadersWhenParentFails() throws Exception {

    Container containerA = mock(Container.class);
    Container containerB = mock(Container.class);
    Container containerC = mock(Container.class);

    when(containerA.loadClassWithoutDependencyCheck("java.lang.Object")).thenThrow(ClassNotFoundException.class);
    when(containerB.loadClassWithoutDependencyCheck("java.lang.Object")).thenThrow(ClassNotFoundException.class);
    when(containerC.loadClassWithoutDependencyCheck("java.lang.Object")).thenThrow(ClassNotFoundException.class);

    List<Container> containerList = new ArrayList<>();
    containerList.add(containerA);
    containerList.add(containerB);
    containerList.add(containerC);

    ClassLoader classLoader = mock(ClassLoader.class);
    when(classLoader.loadClass("java.lang.Object")).thenThrow(ClassNotFoundException.class);

    DependencyClassLoader dependencyClassLoader = new DependencyClassLoader(
        null,
        classLoader,
        containerList
    );

    try {
      dependencyClassLoader.loadClass("java.lang.Object");
      Assert.fail("Should throw ClassNotFoundException");

    } catch (ClassNotFoundException e) {
      // expected
    }

    verify(containerA, times(1)).loadClassWithoutDependencyCheck("java.lang.Object");
    verify(containerB, times(1)).loadClassWithoutDependencyCheck("java.lang.Object");
    verify(containerC, times(1)).loadClassWithoutDependencyCheck("java.lang.Object");
  }

  @Test
  public void loadClassWithoutDependencyCheckSkipsDependencyContainerClassLoadersWhenParentFails() throws Exception {

    Container containerA = mock(Container.class);
    Container containerB = mock(Container.class);
    Container containerC = mock(Container.class);

    when(containerA.loadClassWithoutDependencyCheck("java.lang.Object")).thenThrow(ClassNotFoundException.class);
    when(containerB.loadClassWithoutDependencyCheck("java.lang.Object")).thenThrow(ClassNotFoundException.class);
    when(containerC.loadClassWithoutDependencyCheck("java.lang.Object")).thenThrow(ClassNotFoundException.class);

    List<Container> containerList = new ArrayList<>();
    containerList.add(containerA);
    containerList.add(containerB);
    containerList.add(containerC);

    ClassLoader classLoader = mock(ClassLoader.class);
    when(classLoader.loadClass("java.lang.Object")).thenThrow(ClassNotFoundException.class);

    DependencyClassLoader dependencyClassLoader = new DependencyClassLoader(
        null,
        classLoader,
        containerList
    );

    try {
      dependencyClassLoader.loadClassWithoutDependencyCheck("java.lang.Object");
      Assert.fail("Should throw ClassNotFoundException");

    } catch (ClassNotFoundException e) {
      // expected
    }

    verify(containerA, never()).loadClassWithoutDependencyCheck("java.lang.Object");
    verify(containerB, never()).loadClassWithoutDependencyCheck("java.lang.Object");
    verify(containerC, never()).loadClassWithoutDependencyCheck("java.lang.Object");
  }
}