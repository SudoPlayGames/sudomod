package com.sudoplay.sudoxt.candidate.extractor;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class ZipFileExtractionPathProviderTest {

  @Test
  public void shouldReturnPathDerivedFromCompressedCandidateFilename() {

    Path tempfolder = Paths.get("tempfolder");

    ZipFileExtractionPathProvider pathProvider = new ZipFileExtractionPathProvider(tempfolder, "zip");

    Path temporaryPath = pathProvider.getTemporaryPath(Paths.get("filename.zip"));

    Assert.assertTrue(tempfolder.resolve("filename").equals(temporaryPath));
  }
}
