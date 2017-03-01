package com.sudoplay.sudoext.classloader.asm.callback;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class InjectedCallback {

  public static ICallbackDelegate DELEGATE = NoOpCallbackDelegate.INSTANCE;

  public static void callback_NEW(String type) {
    DELEGATE.callback_NEW(type);
  }

  public static void callback_NEWARRAY(int size, int memorySize) {
    DELEGATE.callback_NEWARRAY(size, memorySize);
  }

  public static void callback_ANEWARRAY(int size, int memorySize) {
    DELEGATE.callback_ANEWARRAY(size, memorySize);
  }

  public static void callback_MULTIANEWARRAY(int[] sizes, int memorySize) {
    DELEGATE.callback_MULTIANEWARRAY(sizes, memorySize);
  }

  public static void callback_INVOKESPECIAL(String owner, String name, String desc) {
    DELEGATE.callback_INVOKESPECIAL(owner, name, desc);
  }

  public static void callback_INVOKEVIRTUAL(String owner, String name, String desc) {
    DELEGATE.callback_INVOKEVIRTUAL(owner, name, desc);
  }

  public static void callback_INVOKESTATIC(String owner, String name, String desc) {
    DELEGATE.callback_INVOKESTATIC(owner, name, desc);
  }

  public static void callback_INVOKEINTERFACE(String owner, String name, String desc) {
    DELEGATE.callback_INVOKEINTERFACE(owner, name, desc);
  }

  public static void callback_INVOKEDYNAMIC(String name, String desc) {
    DELEGATE.callback_INVOKEDYNAMIC(name, desc);
  }

  public static void callback_JUMP() {
    DELEGATE.callback_JUMP();
  }

  public static void callback_ATHROW() {
    DELEGATE.callback_ATHROW();
  }

  public static void callback_TRYCATCH(String type) {
    DELEGATE.callback_TRYCATCH(type);
  }

  public static void registerObject(Object o) {
    DELEGATE.registerObject(o);
  }

  private InjectedCallback() {
    //
  }

}
