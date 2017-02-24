package com.sudoplay.sudoext.api;

import com.sudoplay.sudoext.container.Plugin;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface AncillaryPlugin extends
    Plugin {

  void doStuff();

  int addValues(int a, int b);

}
