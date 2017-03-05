package com.sudoplay.sudoext.classloader.asm.callback;

import com.sudoplay.sudoext.classloader.IContainerClassLoader;

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
