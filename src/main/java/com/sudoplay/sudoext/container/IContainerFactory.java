package com.sudoplay.sudoext.container;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IContainerFactory {

  Container create(
      Path path
  );
}
