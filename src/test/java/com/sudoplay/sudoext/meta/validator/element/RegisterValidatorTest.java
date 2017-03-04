package com.sudoplay.sudoext.meta.validator.element;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class RegisterValidatorTest {

  @Test
  public void isValidShouldReturnTrueWhenJavaFileExists() throws Exception {

    boolean result = new RegisterValidator(null).isValid(
        Paths.get("src/test/resources"),
        Collections.singletonList(
            "SimpleTestClass"
        ),
        null
    );

    Assert.assertTrue(result);
  }

  @Test
  public void isValidShouldReturnTrueWhenClassFileExistsInJarFile() throws Exception {

    Set<String> jarFileSet = new HashSet<>();
    jarFileSet.add("sudomath.jar"); // must be real, validator verifies existence of jar file first

    ZipSearch zipSearch = mock(ZipSearch.class);
    when(zipSearch.findInZip(any(), any())).thenReturn(true);

    boolean result = new RegisterValidator(zipSearch).isValid(
        Paths.get("src/test/resources"),
        Collections.singletonList(
            "wheee!" // doesn't matter what is here, mock will return true
        ),
        jarFileSet
    );

    Assert.assertTrue(result);
  }

}