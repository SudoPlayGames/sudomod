package com.sudoplay.sudomod.mod.candidate;

import com.sudoplay.sudomod.mod.candidate.extractor.ICompressedModCandidateExtractor;
import com.sudoplay.sudomod.mod.candidate.extractor.ITemporaryModPathProvider;
import com.sudoplay.sudomod.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
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
  public List<ModCandidate> extract(List<ModCandidate> modCandidateList) {
    LOG.debug("Entering extract(modCandidateList=[{}])", modCandidateList);

    Path temporaryModPath;
    ModCandidateTemporaryFolder modCandidateTemporaryFolder;
    ModCandidate modCandidate;
    ModCandidateCompressed modCandidateCompressed;
    List<ModCandidate> toAdd;

    toAdd = new ArrayList<>();

    for (Iterator<ModCandidate> it = modCandidateList.iterator(); it.hasNext(); ) {
      modCandidate = it.next();

      if (modCandidate instanceof ModCandidateCompressed) {
        it.remove();

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

          try {
            FileUtils.deleteRecursively(modCandidateTemporaryFolder.getPath());
          } catch (IOException e) {
            LOG.error("Failed to remove temporary folder [{}]", modCandidateTemporaryFolder.getPath(), e);
          }

        } else {
          LOG.info(
              "Successfully extracted compressed mod file [{}] to [{}]",
              modCandidate.getPath(),
              modCandidateTemporaryFolder.getPath()
          );
          toAdd.add(modCandidateTemporaryFolder);
        }
      }
    }

    modCandidateList.addAll(toAdd);

    LOG.debug("Leaving extract(): {}", modCandidateList);
    return modCandidateList;
  }

}
