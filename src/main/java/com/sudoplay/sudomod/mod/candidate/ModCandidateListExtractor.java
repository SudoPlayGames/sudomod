package com.sudoplay.sudomod.mod.candidate;

import com.sudoplay.sudomod.mod.candidate.extractor.ICompressedModCandidateExtractor;
import com.sudoplay.sudomod.mod.candidate.extractor.ITemporaryModPathProvider;
import com.sudoplay.sudomod.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModCandidateListExtractor implements IModCandidateListExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(ModCandidateListExtractor.class);

  private ICompressedModCandidateExtractor compressedModCandidateExtractor;
  private ITemporaryModPathProvider temporaryModPathProvider;

  public ModCandidateListExtractor(
      ICompressedModCandidateExtractor compressedModCandidateExtractor,
      ITemporaryModPathProvider temporaryModPathProvider
  ) {
    this.compressedModCandidateExtractor = compressedModCandidateExtractor;
    this.temporaryModPathProvider = temporaryModPathProvider;
  }

  @Override
  public List<ModCandidate> extract(List<ModCandidate> modCandidateList, List<ModCandidate> store) {
    LOG.debug("Entering extract(modCandidateList, store)");
    LOG.trace("...modCandidateList=[{}]", modCandidateList);
    LOG.trace("...store=[{}]", store);

    Path temporaryModPath;
    ModCandidateTemporaryFolder modCandidateTemporaryFolder;
    ModCandidateCompressed modCandidateCompressed;

    for (ModCandidate modCandidate : modCandidateList) {

      if (modCandidate instanceof ModCandidateCompressed) {
        modCandidateCompressed = (ModCandidateCompressed) modCandidate;

        temporaryModPath = this.temporaryModPathProvider
            .getTemporaryModPath(modCandidateCompressed);

        modCandidateTemporaryFolder = this.compressedModCandidateExtractor
            .extract(
                modCandidateCompressed,
                temporaryModPath
            );

        if (modCandidateTemporaryFolder.hasExtractionError()) {
          LOG.error(
              "Error extracting compressed mod file [{}], skipping",
              modCandidate.getPath(),
              modCandidateTemporaryFolder.getExtractionError()
          );
          // cleanup any lingering artifacts from the failed extraction
          this.deleteRecursively(modCandidateTemporaryFolder);

        } else {
          LOG.info(
              "Successfully extracted compressed mod file [{}] to [{}]",
              modCandidate.getPath(),
              modCandidateTemporaryFolder.getPath()
          );
          // add the temporary folder candidate if it doesn't have an extraction error
          store.add(modCandidateTemporaryFolder);
        }

      } else {
        // make sure folder candidates get back into the new list
        store.add(modCandidate);
      }
    }

    LOG.debug("Leaving extract()");
    LOG.trace("...[{}]", store);
    return store;
  }

  private void deleteRecursively(ModCandidateTemporaryFolder modCandidateTemporaryFolder) {
    Path path = modCandidateTemporaryFolder.getPath();

    if (!Files.exists(path)) {
      return;
    }

    try {
      FileUtils.deleteRecursively(path);

    } catch (IOException e) {
      LOG.error("Failed to remove temporary folder [{}]", path, e);
    }
  }

}
