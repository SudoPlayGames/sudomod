package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.candidate.Candidate;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class CandidateFolderLocatorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnCandidateFoldersOnlyWithMetaFile() throws IOException {

    CandidateFolderLocator locator = new CandidateFolderLocator("meta.json");

    Path path = Paths.get(this.folder.newFolder().toURI());

    Files.createDirectories(path.resolve("folder-a"));
    Files.createDirectories(path.resolve("folder-b"));
    Files.createDirectories(path.resolve("folder-c"));

    Files.newOutputStream(path.resolve("folder-a/meta.json")).close();
    Files.newOutputStream(path.resolve("folder-b/not-meta.json")).close();

    List<Candidate> candidateList = locator.locateCandidates(path, new ArrayList<>());

    Assert.assertEquals(1, candidateList.size());
    Assert.assertTrue(candidateList.get(0).getPath().equals(path.resolve("folder-a")));
  }

}
