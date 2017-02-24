package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.extractor.ICompressedCandidateExtractor;
import com.sudoplay.sudoext.candidate.extractor.ITemporaryPathProvider;
import com.sudoplay.sudoext.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateListExtractor implements ICandidateListExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateListExtractor.class);

  private ICompressedCandidateExtractor compressedCandidateExtractor;
  private ITemporaryPathProvider temporaryPathProvider;

  public CandidateListExtractor(
      ICompressedCandidateExtractor compressedCandidateExtractor,
      ITemporaryPathProvider temporaryPathProvider
  ) {
    this.compressedCandidateExtractor = compressedCandidateExtractor;
    this.temporaryPathProvider = temporaryPathProvider;
  }

  @Override
  public List<Candidate> extract(List<Candidate> candidateList, List<Candidate> store) {
    LOG.debug("Entering extract(candidateList, store)");
    LOG.trace("...candidateList=[{}]", candidateList);
    LOG.trace("...store=[{}]", store);

    Path temporaryPath;
    CandidateTemporaryFolder candidateTemporaryFolder;
    CandidateCompressed candidateCompressed;

    for (Candidate candidate : candidateList) {

      if (candidate instanceof CandidateCompressed) {
        candidateCompressed = (CandidateCompressed) candidate;

        temporaryPath = this.temporaryPathProvider
            .getTemporaryPath(candidateCompressed);

        candidateTemporaryFolder = this.compressedCandidateExtractor
            .extract(
                candidateCompressed,
                temporaryPath
            );

        if (candidateTemporaryFolder.hasExtractionError()) {
          LOG.error(
              "Error extracting compressed file [{}], skipping",
              candidate.getPath(),
              candidateTemporaryFolder.getExtractionError()
          );
          // cleanup any lingering artifacts from the failed extraction
          this.deleteRecursively(candidateTemporaryFolder);

        } else {
          LOG.info(
              "Successfully extracted compressed file [{}] to [{}]",
              candidate.getPath(),
              candidateTemporaryFolder.getPath()
          );
          // add the temporary folder candidate if it doesn't have an extraction error
          store.add(candidateTemporaryFolder);
        }

      } else {
        // make sure folder candidates get back into the new list
        store.add(candidate);
      }
    }

    LOG.debug("Leaving extract()");
    LOG.trace("...[{}]", store);
    return store;
  }

  private void deleteRecursively(CandidateTemporaryFolder candidateTemporaryFolder) {
    Path path = candidateTemporaryFolder.getPath();

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
