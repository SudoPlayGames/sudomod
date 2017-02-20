package com.sudoplay.sudomod.mod.candidate.extractor;

import com.sudoplay.sudomod.mod.candidate.ModCandidateCompressed;
import com.sudoplay.sudomod.mod.candidate.ModCandidateTemporaryFolder;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ICompressedModCandidateExtractor {

  /**
   * Extracts the provided compressed mod candidate to a temporary folder and returns a new
   * {@link ModCandidateTemporaryFolder} pointing to the new location. Any exceptions thrown during the extraction
   * process are located in the returned object.
   *
   * @param modCandidate  the candidate to extract
   * @param temporaryPath the temporary mod path for this specific candidate
   * @return a new mod candidate object
   */
  ModCandidateTemporaryFolder extract(ModCandidateCompressed modCandidate, Path temporaryPath);

}
