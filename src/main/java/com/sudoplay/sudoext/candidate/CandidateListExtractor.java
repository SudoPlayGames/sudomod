package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.extractor.IZipFileExtractionPathProvider;
import com.sudoplay.sudoext.candidate.extractor.IZipFileExtractor;
import com.sudoplay.sudoext.util.CloseUtil;
import com.sudoplay.sudoext.util.RecursiveFileRemovalProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts compressed file candidates from the candidate list and provides a new list.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateListExtractor implements
    ICandidateListExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateListExtractor.class);

  private IZipFileExtractor zipFileExtractor;
  private IZipFileExtractionPathProvider zipFileExtractionPathProvider;
  private IInputStreamProvider inputStreamProvider;
  private RecursiveFileRemovalProcessor recursiveFileRemovalProcessor;

  public CandidateListExtractor(
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
  public List<Candidate> extract(List<Candidate> candidateList) {
    LOG.debug("Entering extract(candidateList)");
    LOG.trace("...candidateList=[{}]", candidateList);

    Path extractToPath;
    Candidate newCandidate;
    Path candidatePath;
    InputStream inputStream;
    List<Candidate> result;

    result = new ArrayList<>();

    for (Candidate candidate : candidateList) {

      if (candidate.getType() == Candidate.Type.Compressed) {
        // extract the compressed candidate file to a folder in the temporary folder

        candidatePath = candidate.getPath();

        extractToPath = this.zipFileExtractionPathProvider
            .getTemporaryPath(candidatePath);

        inputStream = null;

        try {
          inputStream = this.inputStreamProvider
              .getInputStream(candidatePath);

          newCandidate = this.zipFileExtractor
              .extract(
                  inputStream,
                  extractToPath
              );

          LOG.info(
              "Successfully extracted compressed file [{}] to [{}]",
              candidatePath,
              newCandidate.getPath()
          );

          // add the folder candidate
          result.add(newCandidate);

        } catch (IOException e) {
          LOG.error("Error extracting compressed file [{}]", candidatePath, e);

          try {
            // cleanup any lingering artifacts from the failed extraction
            this.recursiveFileRemovalProcessor
                .deleteRecursively(extractToPath);

          } catch (IOException ee) {
            LOG.error("Failed to remove temporary folder [{}]", extractToPath, ee);
          }

        } finally {
          CloseUtil.close(inputStream, LOG);
        }

      } else {
        // make sure non-compressed folder candidates get back into the new list
        result.add(candidate);
      }
    }

    LOG.debug("Leaving extract()");
    LOG.trace("...[{}]", result);

    return result;
  }
}
