package com.sudoplay.sudoext.classloader.asm.callback;

import com.sudoplay.sudoext.classloader.asm.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class BudgetCallbackDelegate implements
    ICallbackDelegate {

  private static final Logger LOG = LoggerFactory.getLogger(BudgetCallbackDelegate.class);

  private static final long DEFAULT_MAX_MEMORY_BYTES = 10 * 1024 * 1024;
  private static final long DEFAULT_MAX_INSTRUCTION_SIZE = 5000000;
  private static final int DEFAULT_MAX_ARRAY_SIZE = 1024;
  private static final int DEFAULT_MAX_ARRAY_DIMENSIONS = 4;
  private static final long DEFAULT_MAX_TIME_MILLISECONDS = 10 * 1000;

  private Queue<Long> lastAllocationQueue;
  private Map<Object, Long> allocationMap;
  private ReferenceQueue<Object> referenceQueue;

  private long lastGC;

  private long maxMemoryBytes;
  private long maxInstructions;
  private int maxArraySize;
  private int maxArrayDimensions;
  private long maxTimeMilliseconds;

  private long currentMemoryBytes;
  private long currentInstructions;
  private long startTimeMilliseconds;

  public BudgetCallbackDelegate() {
    this.setMaxMemoryBytes(DEFAULT_MAX_MEMORY_BYTES);
    this.setMaxInstructions(DEFAULT_MAX_INSTRUCTION_SIZE);
    this.setMaxArraySize(DEFAULT_MAX_ARRAY_SIZE);
    this.setMaxArrayDimensions(DEFAULT_MAX_ARRAY_DIMENSIONS);
    this.setMaxTimeMilliseconds(DEFAULT_MAX_TIME_MILLISECONDS);
    this.reset();
  }

  public BudgetCallbackDelegate setMaxInstructions(long value) {
    this.maxInstructions = value;
    return this;
  }

  public BudgetCallbackDelegate setMaxArraySize(int value) {
    this.maxArraySize = value;
    return this;
  }

  public BudgetCallbackDelegate setMaxArrayDimensions(int value) {
    this.maxArrayDimensions = value;
    return this;
  }

  public BudgetCallbackDelegate setMaxTimeMilliseconds(long value) {
    this.maxTimeMilliseconds = value;
    return this;
  }

  public BudgetCallbackDelegate setMaxMemoryBytes(long value) {
    this.maxMemoryBytes = value;
    return this;
  }

  @Override
  public void reset() {
    this.currentMemoryBytes = 0;
    this.currentInstructions = 0;
    this.startTimeMilliseconds = System.currentTimeMillis();
    this.lastAllocationQueue = new ArrayDeque<>();
    this.allocationMap = new HashMap<>();
    this.referenceQueue = new ReferenceQueue<>();
  }

  @Override
  public String report() {
    return null; // TODO
  }

  private void checkTime() {

    if (System.currentTimeMillis() - this.startTimeMilliseconds > this.maxTimeMilliseconds) {
      throw new TimeLimitException(String.format(
          "Time limit of [%s] milliseconds exceeded",
          this.maxTimeMilliseconds
      ));
    }
  }

  private void incrementInstructionCount() {
    this.currentInstructions += 1;

    if (this.currentInstructions > this.maxInstructions) {
      throw new InstructionLimitException(String.format(
          "Instruction limit of [%s] instructions exceeded",
          this.maxInstructions
      ));
    }
  }

  private void checkArraySize(int size) {

    if (size < 0) {
      throw new NegativeArraySizeException();
    }

    if (size > this.maxArraySize) {
      throw new ArraySizeLimitException(String.format(
          "Array size [%s] is in excess of limit [%s]",
          size,
          this.maxArraySize
      ));
    }
  }

  private void checkArrayDimensions(int dims) {

    if (dims > this.maxArrayDimensions) {
      throw new ArrayDimensionLimitException(String.format(
          "Array dimensions [%s] is in excess of limit [%s]",
          dims,
          this.maxArrayDimensions
      ));
    }
  }

  private void incrementAllocation(long memorySize) {
    memorySize = align(memorySize);

    if (this.currentMemoryBytes + memorySize > 3 * this.maxMemoryBytes / 4) {

      if (System.currentTimeMillis() - this.lastGC > 1) {
        System.gc();
        this.lastGC = System.currentTimeMillis();
      }

      Object reference;

      while ((reference = this.referenceQueue.poll()) != null) {
        Long size = this.allocationMap.get(reference);

        if (size == null) {
          continue;
        }

        this.currentMemoryBytes -= size;
        this.allocationMap.remove(reference);
      }
    }

    if (this.currentMemoryBytes + memorySize > this.maxMemoryBytes) {
      throw new MemoryLimitException(String.format(
          "Maximum memory limit of [%s] bytes exceeded",
          this.maxMemoryBytes
      ));
    }

    this.currentMemoryBytes += memorySize;
    this.lastAllocationQueue.offer(memorySize);
  }

  private long align(long memorySize) {
    return (long) (Math.ceil(memorySize / 8) * 8);
  }

  private long getClassAllocation(String type) {
    LOG.debug("getClassAllocation(): " + type);
    return 8;
  }

  @Override
  public void callback_NEW(String type) {
    this.checkTime();
    this.incrementInstructionCount();
    this.incrementAllocation(this.getClassAllocation(type));
  }

  @Override
  public void callback_NEWARRAY(int size, int memorySize) {
    this.checkTime();
    this.incrementInstructionCount();
    this.checkArraySize(size);
    this.incrementAllocation(12 + size * memorySize);
  }

  @Override
  public void callback_ANEWARRAY(int size, int memorySize) {
    this.checkTime();
    this.incrementInstructionCount();
    this.checkArraySize(size);
    this.incrementAllocation(12 + size * memorySize);
  }

  @Override
  public void callback_MULTIANEWARRAY(int[] sizes, int memorySize) {
    this.checkTime();
    this.incrementInstructionCount();
    this.checkArrayDimensions(sizes.length);

    for (int size : sizes) {
      this.checkArraySize(size);
    }

    long total = 0;
    long previous = 1;

    for (int i = 0; i < sizes.length; i++) {
      total += this.align(previous * (12 + memorySize * sizes[i]));

      if (i > 0) {
        previous *= sizes[i - 1];
      }
    }

    this.incrementAllocation(total);
  }

  @Override
  public void callback_INVOKESPECIAL(String owner, String name, String desc) {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKEVIRTUAL(String owner, String name, String desc) {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKESTATIC(String owner, String name, String desc) {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKEINTERFACE(String owner, String name, String desc) {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_INVOKEDYNAMIC(String name, String desc) {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_JUMP() {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_ATHROW() {
    this.checkTime();
    this.incrementInstructionCount();
  }

  @Override
  public void callback_TRYCATCH(String type) {
    throw new RestrictedUseException("Usage of try/catch block is prohibited in extensions");
  }

  @Override
  public void registerObject(Object o) {
    Long value = this.lastAllocationQueue.poll();
    PhantomReference<Object> reference = new PhantomReference<>(o, this.referenceQueue);

    if (value != null) {
      this.allocationMap.put(reference, value);
    }
  }
}
