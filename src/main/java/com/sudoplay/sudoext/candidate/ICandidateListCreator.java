package com.sudoplay.sudoext.candidate;

import java.util.List;

/**
 * Provides a list of candidates.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public interface ICandidateListCreator {

  /**
   * Creates a new list of candidates.
   *
   * @return a new list of candidates
   */
  List<Candidate> createCandidateList();
}
