package com.sudoplay.sudoxt.container;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 3/4/2017.
 */
public interface IContainerFactory {

  Container create(
      String id,
      Path path, Map<String, String> registeredPluginMap,
      Set<String> preloadSet
  );
}
