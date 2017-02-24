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
public class CandidateCompressedFileLocatorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnCandidateFoldersOnlyWithMetaFile() throws IOException {

    CandidateCompressedFileLocator locator = new CandidateCompressedFileLocator(
        "meta.json",
        "zip"
    );

    Path target = Paths.get(this.folder.newFolder().toURI());

    Files.copy(Paths.get("src/test/resources/compressed-file.zip"), target.resolve("compressed-file.zip"));
    Files.copy(Paths.get("src/test/resources/compressed-file-with-meta.zip"), target.resolve
        ("compressed-file-with-meta.zip"));

    List<Candidate> candidateList = locator.locateCandidates(target, new ArrayList<>());

    Assert.assertEquals(1, candidateList.size());
    Assert.assertTrue(candidateList.get(0).getPath().equals(target.resolve("compressed-file-with-meta.zip")));
  }

}
