package com.sudoplay.sudomod.folder;

import com.sudoplay.sudomod.ModServiceException;
import com.sudoplay.sudomod.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class TempFolderLifecycleEventHandler implements
    IFolderLifecycleInitializeEventHandler,
    IFolderLifecycleDisposeEventHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TempFolderLifecycleEventHandler.class);

  private Path folder;

  public TempFolderLifecycleEventHandler(Path folder) {
    this.folder = folder;
  }

  @Override
  public void onInitialize() throws ModServiceException {

    try {

      // cleanup
      deleteRecursively(this.folder);

      // create
      Files.createDirectories(this.folder);

    } catch (IOException e) {
      LOG.error("Error creating folder [{}]", this.folder, e);
    }

    // check
    if (!Files.exists(this.folder)) {
      ModServiceException e = new ModServiceException(String.format(
          "Folder doesn't exist [%s]",
          this.folder
      ));
      LOG.error("", e);
      throw e;
    }
  }

  @Override
  public void onDispose() {

    try {
      this.deleteRecursively(this.folder);

    } catch (IOException e) {
      LOG.error("Error deleting folder [{}]", this.folder, e);
    }
  }

  private void deleteRecursively(Path folder) throws IOException {

    if (Files.exists(folder)) {
      FileUtils.deleteRecursively(folder);
    }
  }
}
