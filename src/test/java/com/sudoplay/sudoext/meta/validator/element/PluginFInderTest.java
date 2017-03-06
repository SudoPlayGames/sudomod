package com.sudoplay.sudoext.meta.validator.element;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class PluginFinderTest {

  @Test
  public void hasPluginShouldReturnTrueWhenJavaFileExists() throws Exception {
    PluginFinder pluginFinder = new PluginFinder(null);
    boolean result = pluginFinder.hasPlugin("SimpleTestClass", Paths.get("src/test/resources"), null);
    Assert.assertTrue(result);
  }

  @Test
  public void isValidShouldReturnTrueWhenClassFileExistsInJarFile() throws Exception {

    Set<String> jarFileSet = new HashSet<>();
    jarFileSet.add("sudomath.jar"); // must be real, verifies existence of jar file first

    ZipSearch zipSearch = mock(ZipSearch.class);
    when(zipSearch.findInZip(any(), any())).thenReturn(true);
    PluginFinder pluginFinder = new PluginFinder(zipSearch);

    boolean result = pluginFinder.hasPlugin("whee!", Paths.get("src/test/resources"), jarFileSet);
    Assert.assertTrue(result);
  }

}