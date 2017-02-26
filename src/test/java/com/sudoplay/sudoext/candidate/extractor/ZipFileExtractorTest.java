package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.Candidate;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class ZipFileExtractorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldExtractZipFile() throws Throwable {

    ZipFileExtractor extractor;
    Candidate extracted;

    extractor = new ZipFileExtractor();

    Path temporaryPath = Paths.get(this.folder.newFolder().toURI());
    Files.createDirectories(temporaryPath);

    InputStream inputStream = Files.newInputStream(
        Paths.get("src/test/resources/compressed-file.zip")
    );

    extracted = extractor.extract(inputStream, temporaryPath);

    inputStream.close();

    Assert.assertEquals(temporaryPath, extracted.getPath());

    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-file-0.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-a/test-file-1.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-a/test-file-2.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-b/test-file-3.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-b/test-folder-c/test-file-4.txt")));
    Assert.assertTrue(Files.exists(temporaryPath.resolve("test-folder-b/test-folder-c/test-file-5.txt")));
  }
}
