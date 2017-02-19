package com.sudoplay.sudomod.mod.info;

/**
 * Created by sk3lls on 2/18/2017.
 */
public class DefaultModInfoFactory implements
    IModInfoFactory {

  @Override
  public ModInfo create() {
    return new ModInfo();
  }
}
