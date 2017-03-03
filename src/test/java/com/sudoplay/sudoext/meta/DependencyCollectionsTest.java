package com.sudoplay.sudoext.meta;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class DependencyCollectionsTest {

  @Test
  public void addDependencyShouldAddRequiredBeforeToRequiredAndDependentList() throws Exception {
    Dependency dependency = mock(Dependency.class);
    when(dependency.getLoadOrder()).thenReturn(LoadOrder.RequiredBefore);

    Meta meta = new Meta(null, null);

    meta.addDependency(dependency);

    Assert.assertTrue(meta.getRequiredDependencySet().contains(dependency));
    Assert.assertTrue(meta.getLoadBeforeDependencySet().contains(dependency));
  }

  @Test
  public void addDependencyShouldAddRequiredAfterToRequiredAndDependencyList() throws Exception {
    Dependency dependency = mock(Dependency.class);
    when(dependency.getLoadOrder()).thenReturn(LoadOrder.RequiredAfter);

    Meta meta = new Meta(null, null);

    meta.addDependency(dependency);

    Assert.assertTrue(meta.getRequiredDependencySet().contains(dependency));
    Assert.assertTrue(meta.getLoadAfterDependencySet().contains(dependency));
  }

  @Test
  public void addDependencyShouldAddBeforeToDependentList() throws Exception {
    Dependency dependency = mock(Dependency.class);
    when(dependency.getLoadOrder()).thenReturn(LoadOrder.Before);

    Meta meta = new Meta(null, null);

    meta.addDependency(dependency);

    Assert.assertFalse(meta.getRequiredDependencySet().contains(dependency));
    Assert.assertTrue(meta.getLoadBeforeDependencySet().contains(dependency));
  }

  @Test
  public void addDependencyShouldAddAfterToDependencyList() throws Exception {
    Dependency dependency = mock(Dependency.class);
    when(dependency.getLoadOrder()).thenReturn(LoadOrder.After);

    Meta meta = new Meta(null, null);

    meta.addDependency(dependency);

    Assert.assertFalse(meta.getRequiredDependencySet().contains(dependency));
    Assert.assertTrue(meta.getLoadAfterDependencySet().contains(dependency));
  }

}