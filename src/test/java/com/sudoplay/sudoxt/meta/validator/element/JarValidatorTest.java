package com.sudoplay.sudoxt.meta.validator.element;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class JarValidatorTest {

  @Test
  public void isValidShouldReturnTrueWhenJarsExist() throws Exception {

    Set<String> set = new HashSet<>();
    set.add("mock.jar");
    set.add("mock2.jar");
    set.add("mock3.jar");

    JarValidator validator = new JarValidator();
    Assert.assertTrue(validator.isValidSet(Paths.get("src/test/resources"), set));
  }

  @Test
  public void isValidShouldReturnFalseWhenJarDoesNotExist() throws Exception {

    Set<String> set = new HashSet<>();
    set.add("missing.jar");

    JarValidator validator = new JarValidator();
    Assert.assertFalse(validator.isValidSet(Paths.get("src/test/resources"), set));
  }

}