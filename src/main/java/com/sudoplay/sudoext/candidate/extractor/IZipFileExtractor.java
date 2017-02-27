package com.sudoplay.sudoext.candidate.extractor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IZipFileExtractor {

  /**
   * Extracts the provided compressed file to a temporary folder.
   *
   * @param inputStream   the input stream
   * @param extractToPath the temporary path for this specific candidate
   */
  void extract(@NotNull InputStream inputStream, @NotNull Path extractToPath) throws IOException;

}
