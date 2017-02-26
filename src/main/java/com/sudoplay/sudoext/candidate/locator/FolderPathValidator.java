package com.sudoplay.sudoext.candidate.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class FolderPathValidator implements
    IPathValidator {

  private static final Logger LOG = LoggerFactory.getLogger(FolderPathValidator.class);

  private String metaFilename;

  public FolderPathValidator(String metaFilename) {
    this.metaFilename = metaFilename;
  }

  @Override
  public boolean isPathValid(Path path) throws IOException {
    Path metaPath;
    boolean containsMetaFile;

    metaPath = path.resolve(this.metaFilename);

    containsMetaFile = Files.exists(metaPath) && Files.isRegularFile(metaPath);

    if (!containsMetaFile) {
      LOG.warn(
          "Folder [{}] doesn't contain the meta file [{}]",
          path,
          this.metaFilename
      );
    }

    return containsMetaFile;
  }
}
