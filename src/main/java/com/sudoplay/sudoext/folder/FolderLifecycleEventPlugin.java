package com.sudoplay.sudoext.folder;

import com.sudoplay.sudoext.service.SEServiceInitializationException;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class FolderLifecycleEventPlugin implements IFolderLifecycleEventPlugin {

  private IFolderLifecycleEventHandler[] folderLifecycleEventHandlers;

  public FolderLifecycleEventPlugin(IFolderLifecycleEventHandler[] folderLifecycleEventHandlers) {
    this.folderLifecycleEventHandlers = folderLifecycleEventHandlers;
  }

  @Override
  public void initialize() throws SEServiceInitializationException {

    for (IFolderLifecycleEventHandler eventHandler : this.folderLifecycleEventHandlers) {

      if (eventHandler instanceof IFolderLifecycleInitializeEventHandler) {
        ((IFolderLifecycleInitializeEventHandler) eventHandler).onInitialize();
      }
    }
  }

  @Override
  public void dispose() {

    for (IFolderLifecycleEventHandler eventHandler : this.folderLifecycleEventHandlers) {

      if (eventHandler instanceof IFolderLifecycleDisposeEventHandler) {
        ((IFolderLifecycleDisposeEventHandler) eventHandler).onDispose();
      }
    }
  }

}
