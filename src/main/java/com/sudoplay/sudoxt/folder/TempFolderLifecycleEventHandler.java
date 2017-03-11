package com.sudoplay.sudoxt.folder;

import com.sudoplay.sudoxt.service.SXServiceInitializationException;
import com.sudoplay.sudoxt.util.RecursiveFileRemovalProcessor;
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
  private RecursiveFileRemovalProcessor recursiveFileRemovalProcessor;

  public TempFolderLifecycleEventHandler(
      Path folder,
      RecursiveFileRemovalProcessor recursiveFileRemovalProcessor
  ) {
    this.folder = folder;
    this.recursiveFileRemovalProcessor = recursiveFileRemovalProcessor;
  }

  @Override
  public void onInitialize() throws SXServiceInitializationException {

    try {

      // cleanup
      this.recursiveFileRemovalProcessor
          .deleteRecursively(this.folder);

      // create
      Files.createDirectories(this.folder);

    } catch (IOException e) {
      LOG.error("Error creating folder [{}]", this.folder, e);
    }

    // check
    if (!Files.exists(this.folder)) {
      SXServiceInitializationException e = new SXServiceInitializationException(String.format(
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
      this.recursiveFileRemovalProcessor
          .deleteRecursively(this.folder);

    } catch (IOException e) {
      LOG.error("Error deleting folder [{}]", this.folder, e);
    }
  }
}
