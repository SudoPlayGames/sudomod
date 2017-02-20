package com.sudoplay.sudomod.mod.candidate.locator;

import com.sudoplay.sudomod.mod.ModLoadException;
import com.sudoplay.sudomod.mod.candidate.ModCandidate;
import com.sudoplay.sudomod.mod.candidate.ModCandidateCompressed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.List;

/**
 * Locates mod candidates that are compressed files, verifies that they are a .zip file and that they contain a
 * mod-info.json file.
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
  ) throws ModLoadException {
    LOG.debug("Entering locateModCandidates(modLocation=[{}])", modLocation);

    DirectoryStream<Path> files;

    try {
      // find all .mod files
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**" + this.compressedModFileExtension);
      files = Files.newDirectoryStream(
          modLocation,
          entry -> Files.isRegularFile(entry, this.linkOptions) && matcher.matches(entry)
      );

    } catch (IOException e) {
      LOG.error("Error checking path [{}] for mod files", modLocation);
      throw new ModLoadException(e);
    }

    for (Path file : files) {

      try {
        // check if .zip compressed file
        RandomAccessFile randomAccessFile = new RandomAccessFile(file.toFile(), "r");
        long n = randomAccessFile.readInt();
        randomAccessFile.close();

        if (n == MAGIC_BYTES) {
          // check for mod-info.json
          FileSystem compressedFileSystem = FileSystems.newFileSystem(file, null);
          Path modInfoPath = compressedFileSystem.getPath(this.modInfoFilename);

          if (Files.exists(modInfoPath, this.linkOptions)) {
            store.add(new ModCandidateCompressed(file));
            LOG.debug("Found compressed mod candidate file [{}]", file);
          }

        } else {
          LOG.error("Mod candidate [{}] is not a zip file, skipping", file);
        }

      } catch (IOException e) {
        LOG.error("Error checking mod candidate [{}], skipping", file, e);
      }
    }

    LOG.debug("Leaving locateModCandidates(): [{}]", store);
    return store;
  }
}
