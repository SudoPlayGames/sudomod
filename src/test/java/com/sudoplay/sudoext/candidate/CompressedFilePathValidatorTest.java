package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.CompressedFilePathValidator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class CompressedFilePathValidatorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnTrueWhenFileIsZipFileAndContainsMetaFile() throws IOException {

    Path target = Paths.get(this.folder.newFolder().toURI());

    Files.copy(Paths.get("src/test/resources/compressed-file-with-meta.zip"), target.resolve
        ("compressed-file-with-meta.zip"));

    CompressedFilePathValidator validator = new CompressedFilePathValidator("meta.json");

    boolean result = validator.isPathValid(target.resolve("compressed-file-with-meta.zip"));

    Assert.assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenFileIsZipFileAndContainsNoMetaFile() throws IOException {

    Path target = Paths.get(this.folder.newFolder().toURI());

    Files.copy(Paths.get("src/test/resources/compressed-file.zip"), target.resolve("compressed-file.zip"));

    CompressedFilePathValidator validator = new CompressedFilePathValidator("meta.json");

    boolean result = validator.isPathValid(target.resolve("compressed-file.zip"));

    Assert.assertFalse(result);
  }
}
