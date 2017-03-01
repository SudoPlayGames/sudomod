package com.sudoplay.asmtest;

import java.io.IOException;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class TestFile {

  public String getIntegersString() {

    try {
      throw new IOException();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return "Done!";
  }
}
