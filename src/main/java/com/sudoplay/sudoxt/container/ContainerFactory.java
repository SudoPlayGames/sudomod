package com.sudoplay.sudoxt.container;

import com.sudoplay.sudoxt.classloader.asm.callback.ICallbackDelegateFactory;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class ContainerFactory implements IContainerFactory {

  private IContainerCacheFactory containerCacheFactory;
  private ICallbackDelegateFactory callbackDelegateFactory;
  private PluginInstantiator pluginInstantiator;

  public ContainerFactory(
      IContainerCacheFactory containerCacheFactory,
      ICallbackDelegateFactory callbackDelegateFactory,
      PluginInstantiator pluginInstantiator
  ) {
    this.containerCacheFactory = containerCacheFactory;
    this.callbackDelegateFactory = callbackDelegateFactory;
    this.pluginInstantiator = pluginInstantiator;
  }

  @Override
  public Container create(
      String id,
      Path path,
      Map<String, String> registeredPluginMap,
      Set<String> preloadSet
  ) {
    return new Container(
        id,
        path,
        this.containerCacheFactory,
        this.callbackDelegateFactory,
        this.pluginInstantiator,
        registeredPluginMap,
        preloadSet
    );
  }

}
