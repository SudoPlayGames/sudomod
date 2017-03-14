package mod;

import com.sudoplay.sudoxt.api.LoggingAPI;
import mod.nested.HelloWorldGreeting;
import mod.nested.IGreeting;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class HelloUniverseGreeting implements
    IGreeting {

  public String getGreeting() {
    LoggingAPI.getLog(HelloUniverseGreeting.class).debug("getGreeting() called");
    return new HelloWorldGreeting().getGreeting();
  }
}
