package com.sudoplay.sudoext.candidate;

import java.nio.file.Path;

/**
 * Represents a candidate's non-temporary folder. The path points to the folder location.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateFolder extends
    Candidate {

  public CandidateFolder(Path path) {
    super(path);
  }
}
