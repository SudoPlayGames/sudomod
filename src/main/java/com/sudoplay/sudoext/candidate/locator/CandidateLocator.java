package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.candidate.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Locates, verifies and returns a list of candidates.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class CandidateLocator implements
    ICandidateLocator {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateLocator.class);

  private IPathListProvider pathListProvider;
  private IPathValidator pathValidator;
  private ICandidateFactory candidateFactory;

  public CandidateLocator(
      IPathListProvider pathListProvider,
      IPathValidator pathValidator,
      ICandidateFactory candidateFactory
  ) {
    this.pathListProvider = pathListProvider;
    this.pathValidator = pathValidator;
    this.candidateFactory = candidateFactory;
  }

  @Override
  public List<Candidate> locateCandidates() throws IOException {
    LOG.debug("Entering locateCandidates()");

    List<Candidate> result;
    List<Path> pathList;

    result = new ArrayList<>();

    pathList = this.pathListProvider
        .getPathList();

    for (Path path : pathList) {

      try {

        if (this.pathValidator.isPathValid(path)) {
          result.add(this.candidateFactory.create(path));
          LOG.debug("Found candidate [{}]", path);
        }

      } catch (IOException e) {
        LOG.error("Error validating candidate [{}]", path);
      }
    }

    LOG.info("Found [{}] candidates", result.size());
    LOG.debug("Leaving locateCandidates()");
    LOG.trace("...[{}]", result);
    return result;
  }
}
