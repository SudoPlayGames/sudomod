package com.sudoplay.sudoext.sort;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class GraphNode {

  private Object wrappedObject;

  public GraphNode() {
    //
  }

  public GraphNode(Object wrappedObject) {
    this.wrappedObject = wrappedObject;
  }

  public <T> T get() {
    //noinspection unchecked
    return (T) this.wrappedObject;
  }

  @Override
  public String toString() {
    return this.wrappedObject != null ? this.wrappedObject.toString() : "null";
  }
}
