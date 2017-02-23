package com.sudoplay.sudomod.mod.container;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IModContainerFactory {

  ModContainer create(
      Path modPath
  );
}
