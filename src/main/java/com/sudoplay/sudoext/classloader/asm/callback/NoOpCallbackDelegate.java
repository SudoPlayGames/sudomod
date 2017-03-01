package com.sudoplay.sudoext.classloader.asm.callback;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class NoOpCallbackDelegate implements
    ICallbackDelegate {

  public static final ICallbackDelegate INSTANCE = new NoOpCallbackDelegate();

  private NoOpCallbackDelegate() {
    //
  }

  @Override
  public void reset() {
    // no-op
  }

  @Override
  public String report() {
    return "";
  }

  @Override
  public void callback_NEW(String type) {
    // no-op
  }

  @Override
  public void callback_NEWARRAY(int size, int memorySize) {
    // no-op
  }

  @Override
  public void callback_ANEWARRAY(int size, int memorySize) {
    // no-op
  }

  @Override
  public void callback_MULTIANEWARRAY(int[] sizes, int memorySize) {
    // no-op
  }

  @Override
  public void callback_INVOKESPECIAL(String owner, String name, String desc) {
    // no-op
  }

  @Override
  public void callback_INVOKEVIRTUAL(String owner, String name, String desc) {
    // no-op
  }

  @Override
  public void callback_INVOKESTATIC(String owner, String name, String desc) {
    // no-op
  }

  @Override
  public void callback_INVOKEINTERFACE(String owner, String name, String desc) {
    // no-op
  }

  @Override
  public void callback_INVOKEDYNAMIC(String name, String desc) {
    // no-op
  }

  @Override
  public void callback_JUMP() {
    // no-op
  }

  @Override
  public void callback_ATHROW() {
    // no-op
  }

  @Override
  public void callback_TRYCATCH(String type) {
    // no-op
  }

  @Override
  public void registerObject(Object o) {
    // no-op
  }
}
