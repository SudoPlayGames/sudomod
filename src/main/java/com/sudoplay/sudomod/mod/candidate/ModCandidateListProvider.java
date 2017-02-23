package com.sudoplay.sudomod.mod.candidate;

import com.sudoplay.sudomod.mod.candidate.locator.IModCandidateLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModCandidateListProvider implements
    IModCandidateListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(ModCandidateListProvider.class);

  private IModCandidateLocator[] modCandidateLocators;
  private Path modLocation;

  public ModCandidateListProvider(
      IModCandidateLocator[] modCandidateLocators,
      Path modLocation
  ) {
    this.modCandidateLocators = modCandidateLocators;
    this.modLocation = modLocation;
  }

  @Override
  public List<ModCandidate> getModCandidateList(
      List<ModCandidate> store
  ) {
    LOG.debug("Entering getModCandidateList(store)");
    LOG.debug("Looking for mod candidates in [{}] using [{}] locators", this.modLocation, this.modCandidateLocators
        .length);
    LOG.trace("...store=[{}]", store);
    LOG.trace("...modCandidateLocators=[{}]", this.modCandidateLocators);

    for (IModCandidateLocator locator : this.modCandidateLocators) {

      try {
        locator.locateModCandidates(this.modLocation, store);

      } catch (IOException e) {
        LOG.error("Error locating mod candidates with [{}]", locator, e);
      }
    }

    LOG.info("Found a total of [{}] mod candidates in [{}]", store.size(), this.modLocation);
    LOG.debug("Leaving getModCandidateList()");
    LOG.trace("...[{}]", store);
    return store;
  }
}
