package com.sudoplay.sudoext.candidate;

import java.util.List;

/**
 * Extracts compressed file candidates from the candidate list and provides a new list.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public interface ICandidateListExtractor {

  /**
   * Returns a new candidate list after extracting and replacing candidate entries with the extracted version.
   * Compressed candidates that can't be decompressed are not included in the new list.
   *
   * @param candidateList the candidate list containing both folder candidates and compressed candidates
   * @return a new candidate list consisting of folder candidates only
   */
  List<Candidate> extract(List<Candidate> candidateList);
}
