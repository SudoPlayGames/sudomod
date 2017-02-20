package com.sudoplay.sudomod.mod.candidate;

import java.nio.file.Path;

/**
 * Represents a mod candidate's non-temporary folder. The path points to the folder location.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class ModCandidateFolder extends
    ModCandidate {

  public ModCandidateFolder(Path path) {
    super(path);
  }
}
