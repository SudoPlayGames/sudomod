package mod;

import com.sudoplay.sudoxt.api.LoggerAPI;
import com.sudoplay.sudoxt.api.LoggingAPI;
import com.sudoplay.sudoxt.api.Plugin;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModPlugin implements
    Plugin {

  private static final LoggerAPI LOG = LoggingAPI.getLog(ModPlugin.class);

  @Override
  public void onGreeting() {

    LOG.info("Hello from mod-c");
    LOG.info("Now die.");

    while (true) {
    }
  }
}
