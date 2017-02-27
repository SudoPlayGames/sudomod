package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.Candidate;

import java.io.IOException;
import java.util.List;

/**
 * Locates candidates.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface ICandidateProvider {

  /**
   * Returns a new list of candidates.
   *
   * @return a new list of candidates
   * @throws IOException
   */
  List<Candidate> getCandidates() throws IOException;
}
