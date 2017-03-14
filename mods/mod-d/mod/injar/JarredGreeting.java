package mod.injar;

import mod.nested.IGreeting;

/**
 * Created by codetaylor on 3/14/2017.
 */
public class JarredGreeting implements
    IGreeting{

  @Override
  public String getGreeting() {
    return "Hello from inside a jar!";
  }
}
