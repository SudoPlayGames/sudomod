package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/26/2017.
 */
public interface ICallbackDelegate {

  void callback_NEW();

  void callback_NEWARRAY(int size);

  void callback_ANEWARRAY(int size);

  void callback_MULTIANEWARRAY(int dims, int[] sizes);

  void callback_INVOKESPECIAL();

  void callback_INVOKEVIRTUAL();

  void callback_INVOKESTATIC();

  void callback_INVOKEINTERFACE();

  void callback_INVOKEDYNAMIC();

  void callback_JUMP();

  void callback_ATHROW();

}
