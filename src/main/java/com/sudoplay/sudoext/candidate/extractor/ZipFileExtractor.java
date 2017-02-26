package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.Candidate;
import org.jetbrains.annotations.NotNull;
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
 * Extracts a {@link InputStream} to the provided {@link Path}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class ZipFileExtractor implements
    IZipFileExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(ZipFileExtractor.class);
  private static final int BUFFER_SIZE = 4096;

  @Override
  public Candidate extract(
      @NotNull InputStream inputStream,
      @NotNull Path extractToPath
  ) throws IOException {
    LOG.debug("Entering extract(inputStream, extractToPath)");
    LOG.trace("...inputStream=[{}]", inputStream);
    LOG.trace("...extractToPath=[{}]", extractToPath);

    ZipEntry zipEntry;
    Path extractedFilePath;
    ZipInputStream zipInputStream;
    Path parent;
    BufferedOutputStream bufferedOutputStream;
    byte[] buffer;
    int read;

    zipInputStream = new ZipInputStream(inputStream);

    Files.createDirectories(extractToPath);

    while ((zipEntry = zipInputStream.getNextEntry()) != null) {
      extractedFilePath = extractToPath.resolve(zipEntry.getName());

      if (zipEntry.isDirectory()) {
        Files.createDirectories(extractedFilePath);

      } else {
        parent = extractedFilePath.getParent();

        if (parent != null) {
          Files.createDirectories(parent);
        }

        bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(extractedFilePath));
        buffer = new byte[BUFFER_SIZE];

        while ((read = zipInputStream.read(buffer)) != -1) {
          bufferedOutputStream.write(buffer, 0, read);
        }

        bufferedOutputStream.close();
      }

      zipInputStream.closeEntry();
    }

    return new Candidate(extractToPath, Candidate.Type.Folder);
  }
}
