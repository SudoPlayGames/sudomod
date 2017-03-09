package com.sudoplay.sudoxt.sort;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class GraphNode<T> {

  private Object wrappedObject;

  public GraphNode() {
    //
  }

  public GraphNode(Object wrappedObject) {
    this.wrappedObject = wrappedObject;
  }

  public T get() {
    //noinspection unchecked
    return (T) this.wrappedObject;
  }

  @Override
  public String toString() {
    return this.wrappedObject != null ? this.wrappedObject.toString() : "null";
  }
}
