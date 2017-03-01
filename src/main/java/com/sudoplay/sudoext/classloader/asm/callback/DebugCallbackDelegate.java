package com.sudoplay.sudoext.classloader.asm.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class DebugCallbackDelegate implements
    ICallbackDelegate {

  private static final Logger LOG = LoggerFactory.getLogger(DebugCallbackDelegate.class);

  private Map<String, Integer> countMap;

  public DebugCallbackDelegate() {
    this.reset();
  }

  private void increment(String key) {
    Integer i = countMap.get(key);

    if (i == null) {
      i = 0;
    }

    this.countMap.put(key, ++i);
  }

  @Override
  public String report() {

    StringBuilder stringBuilder = new StringBuilder("{ ");

    for (String key : this.countMap.keySet()) {
      stringBuilder.append(key).append(":").append(this.countMap.get(key)).append(" ");
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  @Override
  public void reset() {
    this.countMap = new LinkedHashMap<>();
  }

  @Override
  public void callback_NEW(String type) {
    LOG.debug("callback_NEW(" + type + ")");
    this.increment("callback_NEW");
  }

  @Override
  public void callback_NEWARRAY(int size, int memorySize) {
    LOG.debug("callback_NEWARRAY(" + size + ")");
    this.increment("callback_NEWARRAY");
  }

  @Override
  public void callback_ANEWARRAY(int size, int memorySize) {
    LOG.debug("callback_ANEWARRAY(" + size + ")");
    this.increment("callback_ANEWARRAY");
  }

  @Override
  public void callback_MULTIANEWARRAY(int[] sizes, int memorySize) {
    LOG.debug("callback_MULTIANEWARRAY(" + Arrays.toString(sizes) + ", " + memorySize + ")");
    this.increment("callback_MULTIANEWARRAY");
  }

  @Override
  public void callback_INVOKESPECIAL(String owner, String name, String desc) {
    LOG.debug("callback_INVOKESPECIAL(" + owner + ", " + name + ", " + desc + ")");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKEVIRTUAL(String owner, String name, String desc) {
    LOG.debug("callback_INVOKEVIRTUAL(" + owner + ", " + name + ", " + desc + ")");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKESTATIC(String owner, String name, String desc) {
    LOG.debug("callback_INVOKESTATIC(" + owner + ", " + name + ", " + desc + ")");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKEINTERFACE(String owner, String name, String desc) {
    LOG.debug("callback_INVOKEINTERFACE(" + owner + ", " + name + ", " + desc + ")");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKEDYNAMIC(String name, String desc) {
    LOG.debug("callback_INVOKEDYNAMIC(" + name + ", " + desc + ")");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_JUMP() {
    LOG.debug("callback_JUMP");
    this.increment("callback_JUMP");
  }

  @Override
  public void callback_ATHROW() {
    LOG.debug("callback_ATHROW");
    this.increment("callback_ATHROW");
  }

  @Override
  public void callback_TRYCATCH(String type) {
    LOG.debug("callback_TRYCATCH(" + type + ")");
    this.increment("callback_TRYCATCH");
  }

  @Override
  public void registerObject(Object o) {
    LOG.debug("registerObject(" + o + ")");
    this.increment("registerObject");
  }
}
