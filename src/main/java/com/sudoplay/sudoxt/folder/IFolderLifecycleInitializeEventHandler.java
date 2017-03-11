package com.sudoplay.sudoxt.folder;

import com.sudoplay.sudoxt.service.SXServiceInitializationException;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleInitializeEventHandler extends
    IFolderLifecycleEventHandler {

  void onInitialize() throws SXServiceInitializationException;
}
