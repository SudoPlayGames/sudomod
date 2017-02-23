package com.sudoplay.sudomod.api.impl;

import com.sudoplay.sudomod.api.LoggingAPI;
import org.slf4j.Logger;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Slf4jLoggingAPI_Impl implements LoggingAPI {

  private Logger log;

  public Slf4jLoggingAPI_Impl(Logger log) {
    this.log = log;
  }

  @Override
  public String getName() {
    return this.log.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return this.log.isTraceEnabled();
  }

  @Override
  public void trace(String msg) {
    this.log.trace(msg);
  }

  @Override
  public void trace(String format, Object arg) {
    this.log.trace(format, arg);
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    this.log.trace(format, arg1, arg2);
  }

  @Override
  public void trace(String format, Object... arguments) {
    this.log.trace(format, arguments);
  }

  @Override
  public void trace(String msg, Throwable t) {
    this.log.trace(msg, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return this.log.isDebugEnabled();
  }

  @Override
  public void debug(String msg) {
    this.log.debug(msg);
  }

  @Override
  public void debug(String format, Object arg) {
    this.log.debug(format, arg);
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    this.log.debug(format, arg1, arg2);
  }

  @Override
  public void debug(String format, Object... arguments) {
    this.log.debug(format, arguments);
  }

  @Override
  public void debug(String msg, Throwable t) {
    this.log.debug(msg, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return this.log.isInfoEnabled();
  }

  @Override
  public void info(String msg) {
    this.log.info(msg);
  }

  @Override
  public void info(String format, Object arg) {
    this.log.info(format, arg);
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    this.log.info(format, arg1, arg2);
  }

  @Override
  public void info(String format, Object... arguments) {
    this.log.info(format, arguments);
  }

  @Override
  public void info(String msg, Throwable t) {
    this.log.info(msg, t);
  }

  @Override
  public boolean isWarnEnabled() {
    return this.log.isWarnEnabled();
  }

  @Override
  public void warn(String msg) {
    this.log.warn(msg);
  }

  @Override
  public void warn(String format, Object arg) {
    this.log.warn(format, arg);
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    this.log.warn(format, arg1, arg2);
  }

  @Override
  public void warn(String format, Object... arguments) {
    this.log.warn(format, arguments);
  }

  @Override
  public void warn(String msg, Throwable t) {
    this.log.warn(msg, t);
  }

  @Override
  public boolean isErrorEnabled() {
    return this.log.isErrorEnabled();
  }

  @Override
  public void error(String msg) {
    this.log.error(msg);
  }

  @Override
  public void error(String format, Object arg) {
    this.log.error(format, arg);
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    this.log.error(format, arg1, arg2);
  }

  @Override
  public void error(String format, Object... arguments) {
    this.log.error(format, arguments);
  }

  @Override
  public void error(String msg, Throwable t) {
    this.log.error(msg, t);
  }

}
