package com.sudoplay.sudomod.mod.candidate.extractor;

import com.sudoplay.sudomod.mod.candidate.ModCandidateCompressed;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class TemporaryModPathProvider implements ITemporaryModPathProvider {

  private Path temporaryPath;
  private String compressedModExtension;

  public TemporaryModPathProvider(Path temporaryPath, String compressedModExtension) {
    this.temporaryPath = temporaryPath;
    this.compressedModExtension = compressedModExtension;
  }

  @Override
  public Path getTemporaryModPath(ModCandidateCompressed modCandidateCompressed) {
    String folderName = modCandidateCompressed
        .getPath()
        .getFileName()
        .toString()
        .replace(this.compressedModExtension, "");

    return this.temporaryPath.resolve(folderName);
  }

}
