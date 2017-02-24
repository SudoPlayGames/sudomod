package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.locator.ICandidateLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateListProvider implements
    ICandidateListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateListProvider.class);

  private ICandidateLocator[] candidateLocators;
  private Path location;

  public CandidateListProvider(
      ICandidateLocator[] candidateLocators,
      Path location
  ) {
    this.candidateLocators = candidateLocators;
    this.location = location;
  }

  @Override
  public List<Candidate> getCandidateList(
      List<Candidate> store) {
    LOG.debug("Entering getCandidateList(store)");
    LOG.debug("Looking for candidates in [{}] using [{}] locators", this.location, this.candidateLocators
        .length);
    LOG.trace("...store=[{}]", store);
    LOG.trace("...candidateLocators=[{}]", this.candidateLocators);

    for (ICandidateLocator locator : this.candidateLocators) {

      try {
        locator.locateCandidates(this.location, store);

      } catch (IOException e) {
        LOG.error("Error locating candidates with [{}]", locator, e);
      }
    }

    LOG.info("Found a total of [{}] candidates in [{}]", store.size(), this.location);
    LOG.debug("Leaving getCandidateList()");
    LOG.trace("...[{}]", store);
    return store;
  }
}
