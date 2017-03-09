package com.sudoplay.sudoxt.classloader.intercept;

import com.sudoplay.sudoxt.container.SandboxPathProvider;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class SandboxPathProviderStaticInjector extends
    StaticInjector<SandboxPathProvider> {

  public SandboxPathProviderStaticInjector() {
    super(
        SandboxPathProvider.class,
        container -> new SandboxPathProvider(container.getPath())
    );
  }
}
