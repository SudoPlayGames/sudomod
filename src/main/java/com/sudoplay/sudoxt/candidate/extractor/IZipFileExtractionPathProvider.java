package com.sudoplay.sudoxt.candidate.extractor;

import java.nio.file.Path;

/**
 * Responsible for creating a path suitable for compressed candidate extraction.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public interface IZipFileExtractionPathProvider {

  /**
   * Takes a compressed filename and returns a path to extract to.
   *
   * @param path the compressed file's name
   * @return an extraction path
   */
  Path getTemporaryPath(Path path);
}
