package com.sudoplay.sudoext.candidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides a list of candidates.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateListProvider implements
    ICandidateListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(CandidateListProvider.class);

  private ICandidateProvider[] candidateProviders;

  public CandidateListProvider(
      ICandidateProvider[] candidateProviders
  ) {
    this.candidateProviders = candidateProviders;
  }

  @Override
  public List<Candidate> getCandidateList() {
    LOG.debug("Entering getCandidateList()");
    LOG.debug(
        "Looking for candidates using [{}] providers",
        this.candidateProviders.length
    );
    LOG.trace("...candidateProviders=[{}]", Arrays.toString(this.candidateProviders));

    List<Candidate> result = new ArrayList<>();

    for (ICandidateProvider provider : this.candidateProviders) {

      try {
        result.addAll(provider.getCandidates());

      } catch (IOException e) {
        LOG.error("Error locating candidates with [{}]", provider, e);
      }
    }

    LOG.info("Found a total of [{}] candidates", result.size());
    LOG.debug("Leaving createCandidateList()");
    LOG.trace("...[{}]", result);
    return result;
  }
}
