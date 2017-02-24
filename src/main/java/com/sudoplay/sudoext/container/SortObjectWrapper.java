package com.sudoplay.sudoext.container;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class SortObjectWrapper {

  private Object wrappedObject;

  public SortObjectWrapper() {
    //
  }

  public SortObjectWrapper(Object wrappedObject) {
    this.wrappedObject = wrappedObject;
  }

  public <T> T getWrappedObject() {
    //noinspection unchecked
    return (T) this.wrappedObject;
  }

  @Override
  public String toString() {
    return this.wrappedObject != null ? this.wrappedObject.toString() : "null";
  }
}
