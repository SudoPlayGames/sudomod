package com.sudoplay.sudoext.container;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IContainerCache {

  <T> void put(String resourceString, T object);

  <T> T get(String resourceString);

}
