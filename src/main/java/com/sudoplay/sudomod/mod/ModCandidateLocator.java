package com.sudoplay.sudomod.mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModCandidateLocator implements
    IModCandidateLocator {

  private static final Logger LOG = LoggerFactory.getLogger(ModCandidateLocator.class);

  @Override
  public List<File> locateModCandidates(File modLocation, String modInfoFilename) {
    LOG.debug("Entering locateModCandidates(modLocation=[{}])", modLocation);

    List<File> result = new ArrayList<>();
    File[] files = modLocation.listFiles();

    for (File file : files) {

      if (file.isDirectory()) {
        File modInfoFile = new File(file, modInfoFilename);

        if (modInfoFile.exists() && modInfoFile.isFile()) {
          result.add(file);
          LOG.debug("Found mod candidate folder [{}]", file);

        } else {
          LOG.error("Mod folder [{}] is missing [{}]", file, modInfoFilename);
        }
      }
    }

    LOG.info("Found a total of [{}] mod candidates in [{}]", result.size(), modLocation);
    LOG.debug("Leaving locateModCandidates(): [{}]", result);
    return result;
  }
}
