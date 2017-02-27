package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.FolderPathValidator;
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
public class FolderPathValidatorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnTrueWhenFolderContainsMetaFile() throws IOException {

    Path path = Paths.get(this.folder.newFolder().toURI());

    FolderPathValidator folderPathValidator = new FolderPathValidator("meta.json");

    Files.createDirectories(path.resolve("folder-a"));

    Files.newOutputStream(path.resolve("folder-a/meta.json")).close();

    Assert.assertTrue(folderPathValidator.isPathValid(path.resolve("folder-a")));
  }

  @Test
  public void shouldReturnFalseWhenFolderDoesNotContainMetaFile() throws IOException {

    Path path = Paths.get(this.folder.newFolder().toURI());

    FolderPathValidator folderPathValidator = new FolderPathValidator("meta.json");

    Files.createDirectories(path.resolve("folder-a"));
    Files.createDirectories(path.resolve("folder-b"));

    Files.newOutputStream(path.resolve("folder-a/not-meta.json")).close();

    // should return false when folder has file with wrong name
    Assert.assertFalse(folderPathValidator.isPathValid(path.resolve("folder-a")));

    // should return false when folder is empty
    Assert.assertFalse(folderPathValidator.isPathValid(path.resolve("folder-b")));
  }

}
