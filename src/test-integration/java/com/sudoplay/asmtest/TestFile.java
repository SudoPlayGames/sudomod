package com.sudoplay.asmtest;

/**
 * Created by codetaylor on 2/26/2017.
 */
public class TestFile {

  public String getIntegersString(int maxInteger) {
    String integersString = "";

    for (int i = 0; i < maxInteger; i++) {
      integersString += i;
    }

    int[] arr = new int[5000];

    return integersString;
  }

}
