package com.sudoplay.sudoxt.classloader.asm.callback;

import com.sudoplay.sudoxt.classloader.IContainerClassLoader;

/**
 * Created by codetaylor on 3/3/2017.
 */
public interface ICallbackDelegateFactory {

  ICallbackDelegate create(IContainerClassLoader classLoader);
}
