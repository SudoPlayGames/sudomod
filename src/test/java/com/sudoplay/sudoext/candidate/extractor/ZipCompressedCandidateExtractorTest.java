package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.CandidateTemporaryFolder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class ZipCompressedCandidateExtractorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldExtractZipFile() throws Throwable {

    ZipCompressedCandidateExtractor extractor;
    CandidateTemporaryFolder extracted;

    extractor = new ZipCompressedCandidateExtractor();

    Path temporaryPath = Paths.get(this.folder.newFolder().toURI());
    Files.createDirectories(temporaryPath);

    extracted = extractor.extract(Paths.get("src/test/resources/compressed-file.zip"), temporaryPath);

    if (extracted.hasExtractionError()) {
      throw extracted.getExtractionError();
    }

    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-file-0.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-a/test-file-1.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-a/test-file-2.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-b/test-file-3.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-b/test-folder-c/test-file-4.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-b/test-folder-c/test-file-5.txt")));
  }

  @Test
  public void shouldStoreExceptionInResult() throws Throwable {

    ZipCompressedCandidateExtractor extractor;
    CandidateTemporaryFolder extracted;

    extractor = new ZipCompressedCandidateExtractor();

    Path temporaryPath = Paths.get(this.folder.newFolder().toURI());
    Files.createDirectories(temporaryPath);

    extracted = extractor.extract(Paths.get("src/test/resources/missing-file.zip"), temporaryPath);

    Assert.assertTrue(extracted.hasExtractionError());

    Assert.assertTrue(extracted.getExtractionError() instanceof NoSuchFileException);
  }
}
