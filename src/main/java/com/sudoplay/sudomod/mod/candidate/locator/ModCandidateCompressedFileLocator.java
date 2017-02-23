package com.sudoplay.sudomod.mod.candidate.locator;

import com.sudoplay.sudomod.mod.candidate.ModCandidate;
import com.sudoplay.sudomod.mod.candidate.ModCandidateCompressed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.List;

/**
 * Locates mod candidates that are compressed files, verifies that they are a compressed .zip file and that they
 * contain a mod-info.json file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class ModCandidateCompressedFileLocator extends
    AbstractModCandidateLocator {

  private static final Logger LOG = LoggerFactory.getLogger(ModCandidateCompressedFileLocator.class);

  private static final int MAGIC_BYTES = 0x504B0304;

  private String modInfoFilename;
  private String compressedModFileExtension;

  public ModCandidateCompressedFileLocator(
      String modInfoFilename,
      String compressedModFileExtension,
      boolean followLinks
  ) {
    super(followLinks);
    this.modInfoFilename = modInfoFilename;
    this.compressedModFileExtension = compressedModFileExtension;
  }

  @Override
  public List<ModCandidate> locateModCandidates(
      Path modLocation,
      List<ModCandidate> store
  ) throws IOException {
    LOG.debug("Entering locateModCandidates(modLocation, store)");
    LOG.trace("...modLocation=[{}]", modLocation);
    LOG.trace("...store=[{}]", store);

    for (Path file : this.getCompressedModFilePaths(modLocation)) {

      if (this.isValidFile(file)) {
        LOG.debug("Found compressed mod candidate file [{}]", file);
        store.add(new ModCandidateCompressed(file));
      }
    }

    LOG.debug("Leaving locateModCandidates()");
    LOG.trace("...[{}]", store);
    return store;
  }

  private DirectoryStream<Path> getCompressedModFilePaths(Path modLocation) throws IOException {
    DirectoryStream<Path> files;

    try {
      // find all compressed mod files
      PathMatcher matcher = FileSystems.getDefault()
          .getPathMatcher("glob:**" + this.compressedModFileExtension);

      files = Files.newDirectoryStream(
          modLocation,
          entry -> Files.isRegularFile(entry, this.linkOptions) && matcher.matches(entry)
      );

    } catch (IOException e) {
      LOG.error("Error checking path [{}] for mod files", modLocation);
      throw e;
    }
    return files;
  }

  private boolean isValidFile(Path file) {

    try {

      // check if .zip compressed file
      if (this.isZipFile(file)) {

        if (this.hasModInfoFile(file)) {
          return true;

        } else {
          LOG.error("Mod candidate [{}] is missing a mod info file", file);
        }

      } else {
        LOG.error("Mod candidate [{}] is not a zip file", file);
      }

    } catch (IOException e) {
      LOG.error("Error checking mod candidate [{}]", file, e);
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

  private boolean hasModInfoFile(Path file) throws IOException {
    FileSystem compressedFileSystem;
    Path modInfoPath;

    // check for mod-info.json
    compressedFileSystem = FileSystems.newFileSystem(file, null);
    modInfoPath = compressedFileSystem.getPath(this.modInfoFilename);
    return Files.exists(modInfoPath, this.linkOptions);
  }

}
