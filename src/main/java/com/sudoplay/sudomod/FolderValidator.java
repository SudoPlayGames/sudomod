package com.sudoplay.sudomod;

import com.sudoplay.sudomod.spi.IFolderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Checks if the location exists and is a directory.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class FolderValidator implements
    IFolderValidator {

  private static final Logger LOG = LoggerFactory.getLogger(FolderValidator.class);

  @Override
  public boolean isValidFolder(File folder) {
    LOG.debug("Entering isValidFolder(folder=[{}])", folder);

    boolean isValid = true;

    if (!folder.exists()) {
      LOG.warn("Folder [{}] doesn't exist", folder);
      isValid = false;
    }

    if (isValid && !folder.isDirectory()) {
      LOG.warn("Expected [{}] to be a directory", folder);
      isValid = false;
    }

    LOG.debug("Leaving isValidFolder(): [{}]", isValid);
    return isValid;
  }
}
