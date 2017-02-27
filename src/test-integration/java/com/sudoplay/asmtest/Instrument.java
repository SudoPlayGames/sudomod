package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class Instrument {

  public static IInstrument DELEGATE;

  public static void callback_NEW() {
    DELEGATE.callback_NEW();
  }

  public static void callback_NEWARRAY(int size) {
    DELEGATE.callback_NEWARRAY(size);
  }

  public static void callback_ANEWARRAY() {
    DELEGATE.callback_ANEWARRAY();
  }

  public static void callback_MULTIANEWARRAY(int dims) {
    DELEGATE.callback_MULTIANEWARRAY(dims);
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

}
