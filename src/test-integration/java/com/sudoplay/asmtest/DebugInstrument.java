package com.sudoplay.asmtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class DebugInstrument implements
    IInstrument {

  private static final Logger LOG = LoggerFactory.getLogger(DebugInstrument.class);

  @Override
  public void callback_NEW() {
    LOG.debug("callback_NEW");
  }

  @Override
  public void callback_NEWARRAY(int size) {
    LOG.debug("callback_NEWARRAY(" + size + ")");
  }

  @Override
  public void callback_ANEWARRAY() {
    LOG.debug("callback_ANEWARRAY");
  }

  @Override
  public void callback_MULTIANEWARRAY(int dims) {
    LOG.debug("callback_MULTIANEWARRAY(" + dims + ")");
  }

  @Override
  public void callback_INVOKESPECIAL() {
    LOG.debug("callback_INVOKESPECIAL");
  }

  @Override
  public void callback_INVOKEVIRTUAL() {
    LOG.debug("callback_INVOKEVIRTUAL");
  }

  @Override
  public void callback_INVOKESTATIC() {
    LOG.debug("callback_INVOKESTATIC");
  }

  @Override
  public void callback_INVOKEINTERFACE() {
    LOG.debug("callback_INVOKEINTERFACE");
  }

  @Override
  public void callback_INVOKEDYNAMIC() {
    LOG.debug("callback_INVOKEDYNAMIC");
  }

  @Override
  public void callback_JUMP() {
    LOG.debug("callback_JUMP");
  }

  @Override
  public void callback_ATHROW() {
    LOG.debug("callback_ATHROW");
  }
}
