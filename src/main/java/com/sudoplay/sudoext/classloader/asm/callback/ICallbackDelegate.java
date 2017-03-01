package com.sudoplay.sudoext.classloader.asm.callback;

/**
 * Created by codetaylor on 2/26/2017.
 */
public interface ICallbackDelegate {

  void reset();

  String report();

  void callback_NEW(String type);

  void callback_NEWARRAY(int size, int memorySize);

  void callback_ANEWARRAY(int size, int memorySize);

  void callback_MULTIANEWARRAY(int[] sizes, int memorySize);

  void callback_INVOKESPECIAL(String owner, String name, String desc);

  void callback_INVOKEVIRTUAL(String owner, String name, String desc);

  void callback_INVOKESTATIC(String owner, String name, String desc);

  void callback_INVOKEINTERFACE(String owner, String name, String desc);

  void callback_INVOKEDYNAMIC(String name, String desc);

  void callback_JUMP();

  void callback_ATHROW();

  void callback_TRYCATCH(String type);

  void registerObject(Object o);

}
