package com.sudoplay.sudoext.candidate.locator;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class FileExtensionPathListProviderTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnFilesWithExtensionOnly() throws IOException {

    Path target = Paths.get(this.folder.newFolder().toURI());

    Files.newOutputStream(target.resolve("file-a.zip")).close();
    Files.newOutputStream(target.resolve("file-b.txt")).close();
    Files.createDirectories(target.resolve("directory-a"));

    FileExtensionPathListProvider provider = new FileExtensionPathListProvider(
        target,
        "zip"
    );

    List<Path> pathList = provider.getPathList();

    Assert.assertEquals(1, pathList.size());
    Assert.assertEquals(target.resolve("file-a.zip"), pathList.get(0));
  }

}
