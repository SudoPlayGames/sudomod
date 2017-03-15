package com.sudoplay.sudoxt.service;

import java.util.List;

/**
 * Created by codetaylor on 3/15/2017.
 */
public interface IRegisteredPluginListProvider {
  <P> List<SXPluginReference<P>> getRegisteredPlugins(String name, Class<P> pClass);
}
