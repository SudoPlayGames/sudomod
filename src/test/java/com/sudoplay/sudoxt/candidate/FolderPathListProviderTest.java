package com.sudoplay.sudoxt.candidate;

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
 * Created by codetaylor on 2/24/2017.
 */
public class FolderPathListProviderTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnFoldersOnly() throws IOException {

    Path path = Paths.get(this.folder.newFolder().toURI());

    Files.createDirectories(path.resolve("folder-a"));
    Files.newOutputStream(path.resolve("file-a.txt")).close();

    FolderPathListProvider folderPathListProvider = new FolderPathListProvider(new Path[]{path});

    List<Path> pathList = folderPathListProvider.getPathList();

    Assert.assertEquals(1, pathList.size());
    Assert.assertEquals(path.resolve("folder-a"), pathList.get(0));
  }
}
