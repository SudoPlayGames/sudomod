package mod;

import mod.nested.IGreeting;

/**
 * Created by codetaylor on 3/14/2017.
 */
public class FenceGreeting implements
    IGreeting {

  @Override
  public String getGreeting() {
    return "Greetings from the other side of the fence!";
  }
}
