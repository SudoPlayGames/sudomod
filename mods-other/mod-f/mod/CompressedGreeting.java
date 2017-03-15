package mod;

import mod.nested.IGreeting;

/**
 * Created by codetaylor on 3/14/2017.
 */
public class CompressedGreeting implements
    IGreeting{

  @Override
  public String getGreeting() {
    return "Compressed greeting!";
  }
}
