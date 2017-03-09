package com.sudoplay.sudoxt.meta.validator.element;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class RegisterValidatorTest {

  @Test
  public void isValidShouldReturnTrueWhenAllPluginsFound() throws Exception {

    PluginFinder pluginFinder = mock(PluginFinder.class);
    when(pluginFinder.hasPlugin(eq("TestA"), any(), any())).thenReturn(true);
    when(pluginFinder.hasPlugin(eq("TestB"), any(), any())).thenReturn(true);

    boolean result = new RegisterValidator(pluginFinder).isValid(
        Paths.get("src/test/resources"),
        Arrays.asList(
            "TestA",
            "TestB"
        ),
        null
    );

    Assert.assertTrue(result);
  }

  @Test
  public void isValidShouldReturnTrueWhenAnyPluginNotFound() throws Exception {

    PluginFinder pluginFinder = mock(PluginFinder.class);
    when(pluginFinder.hasPlugin(eq("TestA"), any(), any())).thenReturn(true);
    when(pluginFinder.hasPlugin(eq("TestB"), any(), any())).thenReturn(false);

    boolean result = new RegisterValidator(pluginFinder).isValid(
        Paths.get("src/test/resources"),
        Arrays.asList(
            "TestA",
            "TestB"
        ),
        null
    );

    Assert.assertFalse(result);
  }

}