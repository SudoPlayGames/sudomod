package com.sudoplay.sudoxt.container;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/8/2017.
 */
public class SandboxPathProviderTest {

  private SandboxPathProvider sandboxPathProvider = new SandboxPathProvider(null);

  @Test
  public void clean() throws Exception {

    test("/test/", "test/");
    test("../test", "test");
    test("../test.txt", "test.txt");
    test("../../../test...txt", "test.txt");
    test("\\test\\test.txt", "test/test.txt");
    test("..\\test\\test.txt", "test/test.txt");
    test("TeSt.txt", "TeSt.txt");
    test(
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_/.",
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_/."
    );
    test("./", "");
    test("C:/system/malicious.exe", "C/system/malicious.exe");

  }

  private void test(String input, String expected) {
    Assert.assertEquals(expected, this.sandboxPathProvider.clean(input));
  }

}