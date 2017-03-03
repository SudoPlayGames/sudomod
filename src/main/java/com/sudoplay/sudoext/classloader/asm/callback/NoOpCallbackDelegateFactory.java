package com.sudoplay.sudoext.classloader.asm.callback;

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
  public ICallbackDelegate create() {
    return NoOpCallbackDelegate.INSTANCE;
  }
}
