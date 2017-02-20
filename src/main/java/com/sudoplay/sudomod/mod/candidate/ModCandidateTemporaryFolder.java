package com.sudoplay.sudomod.mod.candidate;

import java.nio.file.Path;

/**
 * Represents a compressed mod candidate that has been extracted to a temporary folder. The path indicates the
 * temporary location.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class ModCandidateTemporaryFolder extends
    ModCandidate {

  private Throwable extractionError;

  public ModCandidateTemporaryFolder(Path path, Throwable extractionError) {
    super(path);
    this.extractionError = extractionError;
  }

  public boolean hasExtractionError() {
    return this.extractionError != null;
  }

  public Throwable getExtractionError() {
    return this.extractionError;
  }
}
