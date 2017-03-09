package com.sudoplay.sudoxt.candidate;

import com.sudoplay.sudoxt.candidate.extractor.IZipFileExtractionPathProvider;
import com.sudoplay.sudoxt.candidate.extractor.IZipFileExtractor;
import com.sudoplay.sudoxt.util.CloseUtil;
import com.sudoplay.sudoxt.util.RecursiveFileRemovalProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class CompressedFileCandidateProcessor implements
    ICandidateProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(CompressedFileCandidateProcessor.class);

  private IZipFileExtractor zipFileExtractor;
  private IZipFileExtractionPathProvider zipFileExtractionPathProvider;
  private IInputStreamProvider inputStreamProvider;
  private RecursiveFileRemovalProcessor recursiveFileRemovalProcessor;

  public CompressedFileCandidateProcessor(
      IZipFileExtractor zipFileExtractor,
      IZipFileExtractionPathProvider zipFileExtractionPathProvider,
      IInputStreamProvider inputStreamProvider,
      RecursiveFileRemovalProcessor recursiveFileRemovalProcessor
  ) {
    this.zipFileExtractor = zipFileExtractor;
    this.zipFileExtractionPathProvider = zipFileExtractionPathProvider;
    this.inputStreamProvider = inputStreamProvider;
    this.recursiveFileRemovalProcessor = recursiveFileRemovalProcessor;
  }

  @Override
  public Candidate process(Candidate candidate) throws CandidateProcessorException {

    Path candidatePath;
    Path extractToPath;
    InputStream inputStream;

    candidatePath = candidate.getPath();

    extractToPath = this.zipFileExtractionPathProvider
        .getTemporaryPath(candidatePath);

    inputStream = null;

    try {
      inputStream = this.inputStreamProvider
          .getInputStream(candidatePath);

      this.zipFileExtractor
          .extract(
              inputStream,
              extractToPath
          );

      LOG.info(
          "Successfully extracted compressed file [{}] to [{}]",
          candidatePath,
          extractToPath
      );

      return new Candidate(extractToPath);

    } catch (IOException e) {

      try {
        // cleanup any lingering artifacts from the failed extraction
        this.recursiveFileRemovalProcessor
            .deleteRecursively(extractToPath);

      } catch (IOException ee) {
        LOG.error("Failed to remove temporary folder [{}]", extractToPath, ee);
      }

      throw new CandidateProcessorException(String.format("Error extracting compressed file [%s]", candidatePath), e);

    } finally {
      CloseUtil.close(inputStream, LOG);
    }
  }
}
