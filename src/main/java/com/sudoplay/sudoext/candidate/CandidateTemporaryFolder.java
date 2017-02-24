package com.sudoplay.sudoext.candidate;

import java.nio.file.Path;

/**
 * Represents a compressed candidate that has been extracted to a temporary folder. The path indicates the
 * temporary location.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateTemporaryFolder extends
    Candidate {

  private Throwable extractionError;

  public CandidateTemporaryFolder(Path path, Throwable extractionError) {
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
