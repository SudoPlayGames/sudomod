package com.sudoplay.sudoxt.container;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class PluginInstantiator {

  public <P> P instantiate(Class<P> pluginClass) throws IllegalAccessException, InstantiationException {
    return pluginClass.newInstance();
  }
}
