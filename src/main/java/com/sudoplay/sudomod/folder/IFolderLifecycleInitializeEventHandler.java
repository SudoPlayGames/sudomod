package com.sudoplay.sudomod.folder;

import com.sudoplay.sudomod.service.ModServiceInitializationException;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleInitializeEventHandler extends
    IFolderLifecycleEventHandler {

  void onInitialize() throws ModServiceInitializationException;
}
