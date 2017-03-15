package com.sudoplay.sudoxt.service;

/**
 * Created by codetaylor on 3/15/2017.
 */
public interface IPluginProvider {
  <P> SXPluginReference<P> getPlugin(String resourceString, Class<P> tClass);

  <P> SXPluginReference<P> getPlugin(SXResourceLocation resourceLocation, Class<P> tClass);
}
