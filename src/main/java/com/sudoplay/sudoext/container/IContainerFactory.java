package com.sudoplay.sudoext.container;

import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 3/4/2017.
 */
public interface IContainerFactory {

  Container create(
      String id,
      Map<String, String> registeredPluginMap,
      Set<String> preloadSet
  );
}
