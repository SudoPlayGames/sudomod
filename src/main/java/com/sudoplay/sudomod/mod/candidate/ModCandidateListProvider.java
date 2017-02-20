package com.sudoplay.sudomod.mod.candidate;

import com.sudoplay.sudomod.mod.ModLoadException;
import com.sudoplay.sudomod.mod.candidate.locator.IModCandidateLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private String modInfoFilename;

  public ModCandidateListProvider(
      IModCandidateLocator[] modCandidateLocators,
      Path modLocation,
      String modInfoFilename
  ) {
    this.modCandidateLocators = modCandidateLocators;
    this.modLocation = modLocation;
    this.modInfoFilename = modInfoFilename;
  }

  @Override
  public List<ModCandidate> getModCandidateList(
      List<ModCandidate> store
  ) {
    LOG.debug("Entering getModCandidateList(modLocation=[{}], modInfoFilename=[{}], store=[{}])", this.modLocation,
        this.modInfoFilename, store);

    for (IModCandidateLocator locator : this.modCandidateLocators) {

      try {
        locator.locateModCandidates(this.modLocation, store);

      } catch (ModLoadException e) {
        LOG.error("Error locating mod candidates with [{}]", locator, e);
      }
    }

    LOG.info("Found a total of [{}] mod candidates in [{}]", store.size(), this.modLocation);
    LOG.debug("Leaving getModCandidateList(): {}", store);
    return store;
  }
}
