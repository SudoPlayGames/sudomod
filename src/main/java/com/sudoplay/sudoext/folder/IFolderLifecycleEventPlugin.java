package com.sudoplay.sudoext.folder;

import com.sudoplay.sudoext.service.SEServiceInitializationException;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleEventPlugin {

  void initialize() throws SEServiceInitializationException;

  void dispose();
}
