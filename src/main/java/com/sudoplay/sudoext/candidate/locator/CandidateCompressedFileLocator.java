package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.candidate.Candidate;
import com.sudoplay.sudoext.candidate.CandidateCompressed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.List;

/**
 * Locates candidates that are compressed files, verifies that they are a compressed .zip file and that they
 * contain an meta json file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class CandidateCompressedFileLocator extends
    AbstractCandidateLocator {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateCompressedFileLocator.class);

  private static final int MAGIC_BYTES = 0x504B0304;

  private String metaFilename;
  private String compressedFileExtension;

  public CandidateCompressedFileLocator(
      String metaFilename,
      String compressedFileExtension
  ) {
    super(false);
    this.metaFilename = metaFilename;
    this.compressedFileExtension = compressedFileExtension;
  }

  @Override
  public List<Candidate> locateCandidates(
      Path location,
      List<Candidate> store
  ) throws IOException {
    LOG.debug("Entering locateCandidates(location, store)");
    LOG.trace("...location=[{}]", location);
    LOG.trace("...store=[{}]", store);

    for (Path file : this.getCompressedFilePaths(location)) {

      if (this.isValidFile(file)) {
        LOG.debug("Found compressed candidate file [{}]", file);
        store.add(new CandidateCompressed(file));
      }
    }

    LOG.debug("Leaving locateCandidates()");
    LOG.trace("...[{}]", store);
    return store;
  }

  private DirectoryStream<Path> getCompressedFilePaths(Path location) throws IOException {
    DirectoryStream<Path> files;

    try {
      // find all compressed files
      PathMatcher matcher = FileSystems.getDefault()
          .getPathMatcher("glob:**" + this.compressedFileExtension);

      files = Files.newDirectoryStream(
          location,
          entry -> Files.isRegularFile(entry, this.linkOptions) && matcher.matches(entry)
      );

    } catch (IOException e) {
      LOG.error("Error checking path [{}] for files", location);
      throw e;
    }
    return files;
  }

  private boolean isValidFile(Path file) {

    try {

      // check if .zip compressed file
      if (this.isZipFile(file)) {

        if (this.hasMetaFile(file)) {
          return true;

        } else {
          LOG.error("Candidate [{}] is missing a meta file", file);
        }

      } else {
        LOG.error("Candidate [{}] is not a zip file", file);
      }

    } catch (IOException e) {
      LOG.error("Error checking candidate [{}]", file, e);
    }

    return false;
  }

  private boolean isZipFile(Path file) throws IOException {
    RandomAccessFile randomAccessFile;
    long magicBytes;

    randomAccessFile = new RandomAccessFile(file.toFile(), "r");
    magicBytes = randomAccessFile.readInt();
    randomAccessFile.close();
    return magicBytes == MAGIC_BYTES;
  }

  private boolean hasMetaFile(Path file) throws IOException {
    FileSystem compressedFileSystem;
    Path path;

    compressedFileSystem = FileSystems.newFileSystem(file, null);
    path = compressedFileSystem.getPath(this.metaFilename);
    return Files.exists(path, this.linkOptions);
  }

}
