package com.sudoplay.sudoext.candidate;

import java.nio.file.Path;

/**
 * Represents a compressed file. The path points to the compressed file.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateCompressed extends
    Candidate {

  public CandidateCompressed(Path path) {
    super(path);
  }
}
