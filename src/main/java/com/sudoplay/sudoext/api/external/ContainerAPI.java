package com.sudoplay.sudoext.api.external;

import com.sudoplay.sudoext.classloader.intercept.InjectStaticField;
import com.sudoplay.sudoext.classloader.intercept.InterceptClass;

/**
 * Created by codetaylor on 2/24/2017.
 */
@InterceptClass
public class ContainerAPI {

  @InjectStaticField("container-id")
  private static String ID;

  public static String getId() {
    return ID;
  }

  private ContainerAPI() {
    //
  }
}
