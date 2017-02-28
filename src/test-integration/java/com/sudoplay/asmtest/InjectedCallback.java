package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class InjectedCallback {

  public static ICallbackDelegate DELEGATE;

  public static void callback_NEW() {
    DELEGATE.callback_NEW();
  }

  public static void callback_NEWARRAY(int size) {
    DELEGATE.callback_NEWARRAY(size);
  }

  public static void callback_ANEWARRAY(int size) {
    DELEGATE.callback_ANEWARRAY(size);
  }

  public static void callback_MULTIANEWARRAY(int dims, int[] sizes) {
    DELEGATE.callback_MULTIANEWARRAY(dims, sizes);
  }

  public static void callback_INVOKESPECIAL() {
    DELEGATE.callback_INVOKESPECIAL();
  }

  public static void callback_INVOKEVIRTUAL() {
    DELEGATE.callback_INVOKEVIRTUAL();
  }

  public static void callback_INVOKESTATIC() {
    DELEGATE.callback_INVOKESTATIC();
  }

  public static void callback_INVOKEINTERFACE() {
    DELEGATE.callback_INVOKEINTERFACE();
  }

  public static void callback_INVOKEDYNAMIC() {
    DELEGATE.callback_INVOKEDYNAMIC();
  }

  public static void callback_JUMP() {
    DELEGATE.callback_JUMP();
  }

  public static void callback_ATHROW() {
    DELEGATE.callback_ATHROW();
  }

  private InjectedCallback() {
    //
  }

}
