package com.sudoplay.sudoext.candidate;

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
public class CandidateProvider implements
    ICandidateProvider {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateProvider.class);

  private IPathListProvider pathListProvider;
  private IPathValidator pathValidator;
  private ICandidateFactory candidateFactory;
  private ICandidateProcessor candidateProcessor;

  public CandidateProvider(
      IPathListProvider pathListProvider,
      IPathValidator pathValidator,
      ICandidateFactory candidateFactory,
      ICandidateProcessor candidateProcessor
  ) {
    this.pathListProvider = pathListProvider;
    this.pathValidator = pathValidator;
    this.candidateFactory = candidateFactory;
    this.candidateProcessor = candidateProcessor;
  }

  @Override
  public List<Candidate> getCandidates() throws IOException {
    LOG.debug("Entering getCandidates()");

    List<Candidate> result;
    List<Path> pathList;
    Candidate candidate;

    result = new ArrayList<>();

    pathList = this.pathListProvider
        .getPathList();

    for (Path path : pathList) {

      try {

        if (this.pathValidator.isPathValid(path)) {
          candidate = this.candidateFactory.create(path);
          candidate = this.candidateProcessor.process(candidate);
          result.add(candidate);
          LOG.debug("Found candidate [{}]", path);
        }

      } catch (Exception e) {
        LOG.error("Error validating and processing candidate [{}]", path);
      }
    }

    LOG.info("Found [{}] candidates", result.size());
    LOG.debug("Leaving getCandidates()");
    LOG.trace("...[{}]", result);
    return result;
  }
}
