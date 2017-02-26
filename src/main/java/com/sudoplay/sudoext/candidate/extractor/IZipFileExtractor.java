package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.Candidate;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IZipFileExtractor {

  /**
   * Extracts the provided compressed file to a temporary folder and returns a new
   * {@link Candidate} pointing to the new location.
   *
   * @param inputStream   the input stream
   * @param extractToPath the temporary path for this specific candidate
   * @return a new candidate object
   */
  Candidate extract(@NotNull InputStream inputStream, @NotNull Path extractToPath) throws IOException;

}
