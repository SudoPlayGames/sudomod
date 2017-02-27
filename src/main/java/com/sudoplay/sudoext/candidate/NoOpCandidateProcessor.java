package com.sudoplay.sudoext.candidate;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class NoOpCandidateProcessor implements
    ICandidateProcessor {

  @Override
  public Candidate process(Candidate candidate) {
    return candidate;
  }
}
