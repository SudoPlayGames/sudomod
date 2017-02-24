package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.CandidateCompressed;
import com.sudoplay.sudoext.candidate.CandidateTemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class CompressedCandidateExtractor implements
    ICompressedCandidateExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(CompressedCandidateExtractor.class);
  private static final int BUFFER_SIZE = 4096;

  @Override
  public CandidateTemporaryFolder extract(
      CandidateCompressed candidate,
      Path temporaryPath
  ) {
    LOG.debug("Entering extract(candidate=[{}])", candidate);

    Path compressedFilePath;
    InputStream inputStream;
    ZipInputStream zipInputStream;
    ZipEntry zipEntry;
    Throwable extractionError;
    Path extractedFilePath;

    zipInputStream = null;
    extractionError = null;
    compressedFilePath = candidate.getPath();

    try {
      inputStream = Files.newInputStream(compressedFilePath);
      zipInputStream = new ZipInputStream(inputStream);
      Files.createDirectories(temporaryPath);

      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        extractedFilePath = temporaryPath.resolve(zipEntry.getName());

        if (zipEntry.isDirectory()) {
          Files.createDirectories(extractedFilePath);

        } else {
          extract(zipInputStream, extractedFilePath);
        }

        zipInputStream.closeEntry();
      }

    } catch (IOException e) {
      extractionError = e;
    }

    if (zipInputStream != null) {

      try {
        zipInputStream.close();

      } catch (IOException e) {
        LOG.error("Error closing zip stream for [{}]", compressedFilePath, e);
      }
    }

    return new CandidateTemporaryFolder(temporaryPath, extractionError);
  }

  private void extract(ZipInputStream zipInputStream, Path filePath) throws IOException {
    BufferedOutputStream bufferedOutputStream;
    byte[] buffer;
    int read;

    bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(filePath));
    buffer = new byte[BUFFER_SIZE];

    while ((read = zipInputStream.read(buffer)) != -1) {
      bufferedOutputStream.write(buffer, 0, read);
    }

    bufferedOutputStream.close();
  }
}
