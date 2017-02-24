package com.sudoplay.sudoext.folder;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleDisposeEventHandler extends
    IFolderLifecycleEventHandler {

  void onDispose();
}
