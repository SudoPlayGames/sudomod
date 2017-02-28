package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class AccountingCallbackDelegate implements
    ICallbackDelegate {

  private int maxInstructionCount;
  private int currentInstructionCount;
  private int maxArraySize;

  private void incrementInstructionCount() {
    this.currentInstructionCount += 1;

    if (this.currentInstructionCount >= this.maxInstructionCount) {
      throw new InstructionLimitException(String.format(
          "Instruction limit of [%s] instructions exceeded",
          this.maxInstructionCount
      ));
    }
  }

  private void checkArraySize(int size) {

    if (size >= this.maxArraySize) {
      throw new ArraySizeLimitException(String.format(
          "Array size limit of [%s] exceeded",
          this.maxArraySize
      ));
    }
  }

  @Override
  public void callback_NEW() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_NEWARRAY(int size) {
    this.incrementInstructionCount();
    this.checkArraySize(size);
  }

  @Override
  public void callback_ANEWARRAY(int size) {
    this.incrementInstructionCount();
    this.checkArraySize(size);
  }

  @Override
  public void callback_MULTIANEWARRAY(int dims, int[] sizes) {
    // TODO
  }

  @Override
  public void callback_INVOKESPECIAL() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKEVIRTUAL() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKESTATIC() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKEINTERFACE() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKEDYNAMIC() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_JUMP() {
    this.incrementInstructionCount();
  }

  @Override
  public void callback_ATHROW() {
    this.incrementInstructionCount();
  }
}
