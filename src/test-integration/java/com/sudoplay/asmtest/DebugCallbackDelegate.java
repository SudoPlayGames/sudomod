package com.sudoplay.asmtest;

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

  private Map<String, Integer> countMap = new LinkedHashMap<>();

  private void increment(String key) {
    Integer i = countMap.get(key);

    if (i == null) {
      i = 0;
    }

    this.countMap.put(key, ++i);
  }

  public void report() {

    for (String key : this.countMap.keySet()) {
      System.out.println(key + ": " + this.countMap.get(key));
    }
  }

  @Override
  public void callback_NEW() {
    LOG.debug("callback_NEW");
    this.increment("callback_NEW");
  }

  @Override
  public void callback_NEWARRAY(int size) {
    LOG.debug("callback_NEWARRAY(" + size + ")");
    this.increment("callback_NEWARRAY");
  }

  @Override
  public void callback_ANEWARRAY(int size) {
    LOG.debug("callback_ANEWARRAY(" + size + ")");
    this.increment("callback_ANEWARRAY");
  }

  @Override
  public void callback_MULTIANEWARRAY(int dims, int[] sizes) {
    LOG.debug("callback_MULTIANEWARRAY(" + dims + ", " + Arrays.toString(sizes) + ")");
    this.increment("callback_MULTIANEWARRAY");
  }

  @Override
  public void callback_INVOKESPECIAL() {
    LOG.debug("callback_INVOKESPECIAL");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKEVIRTUAL() {
    LOG.debug("callback_INVOKEVIRTUAL");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKESTATIC() {
    LOG.debug("callback_INVOKESTATIC");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKEINTERFACE() {
    LOG.debug("callback_INVOKEINTERFACE");
    this.increment("callback_INVOKE");
  }

  @Override
  public void callback_INVOKEDYNAMIC() {
    LOG.debug("callback_INVOKEDYNAMIC");
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
}
