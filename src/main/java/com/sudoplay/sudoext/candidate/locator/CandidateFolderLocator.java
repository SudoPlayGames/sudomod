package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.candidate.Candidate;
import com.sudoplay.sudoext.candidate.CandidateFolder;
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
public class CandidateFolderLocator extends
    AbstractCandidateLocator {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateFolderLocator.class);

  private String metaFilename;

  public CandidateFolderLocator(String metaFilename) {
    super(false);
    this.metaFilename = metaFilename;
  }

  @Override
  public List<Candidate> locateCandidates(
      Path location,
      List<Candidate> store
  ) throws IOException {
    LOG.debug("Entering locateCandidates(location, store)");
    LOG.trace("...location=[{}]", location);
    LOG.trace("...store=[{}]", store);

    try {
      DirectoryStream<Path> folders = Files.newDirectoryStream(
          location,
          entry -> Files.isDirectory(entry, this.linkOptions)
      );

      for (Path candidateFolder : folders) {
        Path path = candidateFolder.resolve(this.metaFilename);

        if (Files.exists(path, this.linkOptions)
            && Files.isRegularFile(path, this.linkOptions)) {
          store.add(new CandidateFolder(candidateFolder));
          LOG.debug("Found candidate folder [{}]", candidateFolder);

        } else {
          LOG.error("Folder [{}] is missing [{}]", candidateFolder, this.metaFilename);
        }
      }

    } catch (IOException e) {
      LOG.error("Error checking path [{}] for folders", location);
      throw e;
    }

    LOG.debug("Leaving locateCandidates()");
    LOG.trace("...[{}]", store);
    return store;
  }
}
