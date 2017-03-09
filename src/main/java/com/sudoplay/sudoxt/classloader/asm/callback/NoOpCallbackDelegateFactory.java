package com.sudoplay.sudoxt.classloader.asm.callback;

import com.sudoplay.sudoxt.classloader.IContainerClassLoader;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class NoOpCallbackDelegateFactory implements
    ICallbackDelegateFactory {

  public static final NoOpCallbackDelegateFactory INSTANCE = new NoOpCallbackDelegateFactory();

  private NoOpCallbackDelegateFactory() {
    //
  }

  @Override
  public ICallbackDelegate create(IContainerClassLoader classLoader) {
    return NoOpCallbackDelegate.INSTANCE;
  }
}
