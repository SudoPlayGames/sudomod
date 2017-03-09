package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class JarValidatorTest {

  @Test
  public void isValidShouldReturnTrueWhenJarsExist() throws Exception {

    JarValidator validator = new JarValidator();

    Set<String> set = new HashSet<>();
    set.add("mock.jar");

    Meta meta = mock(Meta.class);
    when(meta.getJarFileSet()).thenReturn(set);

    Assert.assertTrue(validator.isValid(meta, Paths.get("src/test/resources"), null));
  }

  @Test
  public void isValidShouldReturnFalseWhenJarDoesNotExist() throws Exception {

    JarValidator validator = new JarValidator();

    Set<String> set = new HashSet<>();
    set.add("missing.jar");

    Meta meta = mock(Meta.class);
    when(meta.getJarFileSet()).thenReturn(set);

    Assert.assertFalse(validator.isValid(meta, Paths.get("src/test/resources"), null));
  }

}