package com.sudoplay.sudomod.folder;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleDisposeEventHandler extends
    IFolderLifecycleEventHandler {

  void onDispose();
}
