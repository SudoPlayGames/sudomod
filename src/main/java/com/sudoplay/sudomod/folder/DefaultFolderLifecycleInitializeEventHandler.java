package com.sudoplay.sudomod.folder;

import com.sudoplay.sudomod.ModServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class DefaultFolderLifecycleInitializeEventHandler implements
    IFolderLifecycleInitializeEventHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultFolderLifecycleInitializeEventHandler.class);

  private Path folder;

  public DefaultFolderLifecycleInitializeEventHandler(Path folder) {
    this.folder = folder;
  }

  @Override
  public void onInitialize() throws ModServiceException {

    try {
      Files.createDirectories(this.folder);

    } catch (IOException e) {
      LOG.error("Error creating folder [{}]", this.folder, e);
    }

    if (!Files.exists(this.folder)) {
      ModServiceException e = new ModServiceException(String.format(
          "Folder doesn't exist [%s]",
          this.folder
      ));
      LOG.error("", e);
      throw e;
    }
  }
}
