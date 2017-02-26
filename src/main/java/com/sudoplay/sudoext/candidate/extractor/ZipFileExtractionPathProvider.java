package com.sudoplay.sudoext.candidate.extractor;

import java.nio.file.Path;

/**
 * Responsible for creating a path suitable for compressed candidate extraction.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class ZipFileExtractionPathProvider implements
    IZipFileExtractionPathProvider {

  private Path temporaryPath;
  private String compressedFileExtension;

  public ZipFileExtractionPathProvider(Path temporaryPath, String compressedFileExtension) {
    this.temporaryPath = temporaryPath;
    this.compressedFileExtension = compressedFileExtension;
  }

  @Override
  public Path getTemporaryPath(Path path) {

    String folderName = path
        .getFileName()
        .toString()
        .replace("." + this.compressedFileExtension, "");

    return this.temporaryPath.resolve(folderName);
  }

}
