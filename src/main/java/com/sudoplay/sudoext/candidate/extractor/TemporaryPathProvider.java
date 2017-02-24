package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.CandidateCompressed;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class TemporaryPathProvider implements ITemporaryPathProvider {

  private Path temporaryPath;
  private String compressedFileExtension;

  public TemporaryPathProvider(Path temporaryPath, String compressedFileExtension) {
    this.temporaryPath = temporaryPath;
    this.compressedFileExtension = compressedFileExtension;
  }

  @Override
  public Path getTemporaryPath(CandidateCompressed candidateCompressed) {
    String folderName = candidateCompressed
        .getPath()
        .getFileName()
        .toString()
        .replace(this.compressedFileExtension, "");

    return this.temporaryPath.resolve(folderName);
  }

}
