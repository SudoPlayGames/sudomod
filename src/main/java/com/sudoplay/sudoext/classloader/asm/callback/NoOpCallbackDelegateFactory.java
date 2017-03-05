package com.sudoplay.sudoext.classloader.asm.callback;

import com.sudoplay.sudoext.classloader.IContainerClassLoader;

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
