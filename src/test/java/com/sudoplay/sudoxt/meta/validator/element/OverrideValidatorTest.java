package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.service.SXResourceLocation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class OverrideValidatorTest {
  @Test
  public void isValidLocalResourceLocationShouldReturnTrueWhenLocalResourceExists() throws Exception {

    PluginFinder pluginFinder = mock(PluginFinder.class);
    when(pluginFinder.hasPlugin(any(), any(), any())).thenReturn(true);

    OverrideValidator validator = new OverrideValidator(pluginFinder);

    boolean result = validator.isValidLocalResourceLocation(null, new ArrayList<>(Arrays.asList(
        new SXResourceLocation("mod-id:scripts.SomePlugin")
    )), null);

    Assert.assertTrue(result);
  }

}