package com.sudoplay.sudoext.candidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class CompressedFilePathValidator implements
    IPathValidator {

  private static final Logger LOG = LoggerFactory.getLogger(CompressedFilePathValidator.class);

  private static final int MAGIC_BYTES = 0x504B0304;

  private String metaFilename;

  public CompressedFilePathValidator(String metaFilename) {
    this.metaFilename = metaFilename;
  }

  @Override
  public boolean isPathValid(Path file) throws IOException {
    RandomAccessFile randomAccessFile;
    long magicBytes;
    FileSystem compressedFileSystem;
    Path path;

    randomAccessFile = new RandomAccessFile(file.toFile(), "r");
    magicBytes = randomAccessFile.readInt();
    randomAccessFile.close();

    if (magicBytes != MAGIC_BYTES) {
      LOG.warn("File [{}] does not appear to be a valid zip file", file);
      return false;
    }

    compressedFileSystem = FileSystems.newFileSystem(file, null);
    path = compressedFileSystem.getPath(this.metaFilename);
    boolean containsMetaFile = Files.exists(path);

    if (!containsMetaFile) {
      LOG.warn(
          "File [{}] does appear to be a valid zip file, but doesn't contain the meta file [{}]",
          path,
          this.metaFilename
      );
    }

    return containsMetaFile;
  }
}
