package com.sudoplay.sudomod.folder;

import com.sudoplay.sudomod.ModServiceException;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IFolderLifecycleEventPlugin {
  void initialize() throws ModServiceException;

  void dispose();
}
