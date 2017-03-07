package com.sudoplay.sudoext.api.internal.proxy;

import com.sudoplay.sudoext.api.external.LogAPI;
import org.slf4j.Logger;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Slf4JLogProxy implements
    LogAPI {

  private Logger log;

  public Slf4JLogProxy(Logger log) {
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
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.trace(msg);
      return null;
    });
  }

  @Override
  public void trace(String format, Object arg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.trace(format, arg);
      return null;
    });
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.trace(format, arg1, arg2);
      return null;
    });
  }

  @Override
  public void trace(String format, Object... arguments) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.trace(format, arguments);
      return null;
    });
  }

  @Override
  public void trace(String msg, Throwable t) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.trace(msg, t);
      return null;
    });
  }

  @Override
  public boolean isDebugEnabled() {
    return this.log.isDebugEnabled();
  }

  @Override
  public void debug(String msg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.debug(msg);
      return null;
    });
  }

  @Override
  public void debug(String format, Object arg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.debug(format, arg);
      return null;
    });
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.debug(format, arg1, arg2);
      return null;
    });
  }

  @Override
  public void debug(String format, Object... arguments) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.debug(format, arguments);
      return null;
    });
  }

  @Override
  public void debug(String msg, Throwable t) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.debug(msg, t);
      return null;
    });
  }

  @Override
  public boolean isInfoEnabled() {
    return this.log.isInfoEnabled();
  }

  @Override
  public void info(String msg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.info(msg);
      return null;
    });
  }

  @Override
  public void info(String format, Object arg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.info(format, arg);
      return null;
    });
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.info(format, arg1, arg2);
      return null;
    });
  }

  @Override
  public void info(String format, Object... arguments) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.info(format, arguments);
      return null;
    });
  }

  @Override
  public void info(String msg, Throwable t) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.info(msg, t);
      return null;
    });
  }

  @Override
  public boolean isWarnEnabled() {
    return this.log.isWarnEnabled();
  }

  @Override
  public void warn(String msg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.warn(msg);
      return null;
    });
  }

  @Override
  public void warn(String format, Object arg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.warn(format, arg);
      return null;
    });
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.warn(format, arg1, arg2);
      return null;
    });
  }

  @Override
  public void warn(String format, Object... arguments) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.warn(format, arguments);
      return null;
    });
  }

  @Override
  public void warn(String msg, Throwable t) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.warn(msg, t);
      return null;
    });
  }

  @Override
  public boolean isErrorEnabled() {
    return this.log.isErrorEnabled();
  }

  @Override
  public void error(String msg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.error(msg);
      return null;
    });
  }

  @Override
  public void error(String format, Object arg) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.error(format, arg);
      return null;
    });
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.error(format, arg1, arg2);
      return null;
    });
  }

  @Override
  public void error(String format, Object... arguments) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.error(format, arguments);
      return null;
    });
  }

  @Override
  public void error(String msg, Throwable t) {
    AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
      Slf4JLogProxy.this.log.error(msg, t);
      return null;
    });
  }

}
