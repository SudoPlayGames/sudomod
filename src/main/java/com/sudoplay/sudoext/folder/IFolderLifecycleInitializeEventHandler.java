package com.sudoplay.sudoext.folder;

import com.sudoplay.sudoext.service.SEServiceInitializationException;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleInitializeEventHandler extends
    IFolderLifecycleEventHandler {

  void onInitialize() throws SEServiceInitializationException;
}
