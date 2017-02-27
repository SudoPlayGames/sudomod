package com.sudoplay.sudoext.candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class DefaultCandidateFactory implements
    ICandidateFactory {

  @Override
  public Candidate create(Path path) {
    return new Candidate(path);
  }
}
