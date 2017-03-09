package com.sudoplay.sudoxt.classloader.asm.callback;

import com.sudoplay.sudoxt.classloader.IContainerClassLoader;

/**
 * Created by codetaylor on 3/5/2017.
 */
public class AccountingCallbackDelegateFactory implements
    ICallbackDelegateFactory {

  @Override
  public ICallbackDelegate create(IContainerClassLoader classLoader) {
    return new AccountingCallbackDelegate(classLoader);
  }
}
