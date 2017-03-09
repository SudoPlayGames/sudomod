package com.sudoplay.sudoxt.meta.validator.element;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class ZipSearchTest {

  @Test
  public void findInZipReturnsTrueWhenFileExists() throws Exception {

    ZipSearch zipSearch = new ZipSearch();

    boolean result = zipSearch.findInZip(
        Paths.get("src/test/resources/sudomath.jar"),
        "com/sudoplay/math/Vector2f.class"
    );

    Assert.assertTrue(result);
  }

  @Test
  public void findInZipReturnsFalseWhenFileDoesNotExists() throws Exception {

    ZipSearch zipSearch = new ZipSearch();

    boolean result = zipSearch.findInZip(
        Paths.get("src/test/resources/sudomath.jar"),
        "pretty/sure/that/NathanFillionHas.class"
    );

    Assert.assertFalse(result);
  }

}