package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegateFactory;

import java.util.Map;

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
      Map<String, String> registeredPluginMap
  ) {
    return new Container(
        id,
        this.containerCacheFactory,
        this.callbackDelegateFactory,
        this.pluginInstantiator,
        registeredPluginMap
    );
  }

}
