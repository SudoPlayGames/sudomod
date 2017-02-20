package com.sudoplay.sudomod.mod.candidate;

import java.nio.file.Path;

/**
 * Represents a compressed mod file. The path points to the compressed file.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class ModCandidateCompressed extends
    ModCandidate {

  public ModCandidateCompressed(Path path) {
    super(path);
  }
}
