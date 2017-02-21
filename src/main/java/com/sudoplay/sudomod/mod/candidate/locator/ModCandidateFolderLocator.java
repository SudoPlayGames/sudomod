package com.sudoplay.sudomod.mod.candidate.locator;

import com.sudoplay.sudomod.mod.candidate.ModCandidate;
import com.sudoplay.sudomod.mod.candidate.ModCandidateFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModCandidateFolderLocator extends
    AbstractModCandidateLocator {

  private static final Logger LOG = LoggerFactory.getLogger(ModCandidateFolderLocator.class);

  private String modInfoFilename;

  public ModCandidateFolderLocator(String modInfoFilename, boolean followLinks) {
    super(followLinks);
    this.modInfoFilename = modInfoFilename;
  }

  @Override
  public List<ModCandidate> locateModCandidates(
      Path modLocation,
      List<ModCandidate> store
  ) throws IOException {
    LOG.debug("Entering locateModCandidates(modLocation=[{}])", modLocation);

    try {
      DirectoryStream<Path> folders = Files.newDirectoryStream(
          modLocation,
          entry -> Files.isDirectory(entry, this.linkOptions)
      );

      for (Path modCandidateFolder : folders) {
        Path modInfoPath = modCandidateFolder.resolve(this.modInfoFilename);

        if (Files.exists(modInfoPath, this.linkOptions)
            && Files.isRegularFile(modInfoPath, this.linkOptions)) {
          store.add(new ModCandidateFolder(modCandidateFolder));
          LOG.debug("Found mod candidate folder [{}]", modCandidateFolder);

        } else {
          LOG.error("Mod folder [{}] is missing [{}]", modCandidateFolder, this.modInfoFilename);
        }
      }

    } catch (IOException e) {
      LOG.error("Error checking path [{}] for mod folders", modLocation);
      throw e;
    }

    LOG.debug("Leaving locateModCandidates(): [{}]", store);
    return store;
  }
}
