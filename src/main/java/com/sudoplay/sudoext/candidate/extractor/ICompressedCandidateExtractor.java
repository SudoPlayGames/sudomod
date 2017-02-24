package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.CandidateTemporaryFolder;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ICompressedCandidateExtractor {

  /**
   * Extracts the provided compressed file to a temporary folder and returns a new
   * {@link CandidateTemporaryFolder} pointing to the new location. Any exceptions thrown during the extraction
   * process are located in the returned object.
   *
   * @param compressedFilePath the candidate to extract
   * @param temporaryPath      the temporary path for this specific candidate
   * @return a new candidate object
   */
  CandidateTemporaryFolder extract(Path compressedFilePath, Path temporaryPath);

}
