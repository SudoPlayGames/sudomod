package com.sudoplay.sudoxt.container;

import com.sudoplay.sudoxt.service.Plugin;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class PluginInstantiator {

  public <P extends Plugin> P instantiate(Class<P> pluginClass) throws IllegalAccessException, InstantiationException {
    return pluginClass.newInstance();
  }
}
