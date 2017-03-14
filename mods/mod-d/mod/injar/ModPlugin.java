package mod.injar;

import com.sudoplay.math.Vector2i;
import com.sudoplay.sudoxt.api.LoggerAPI;
import com.sudoplay.sudoxt.api.LoggingAPI;
import com.sudoplay.sudoxt.api.Plugin;
import mod.HelloUniverseGreeting;
import mod.InceptionGreeting;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModPlugin implements
    Plugin {

  private static final LoggerAPI LOG = LoggingAPI.getLog(ModPlugin.class);

  @Override
  public void onGreeting() {
    LOG.info("From source: " + new InceptionGreeting().getGreeting());
    LOG.info("From source in dependency: " + new HelloUniverseGreeting().getGreeting());
    LOG.info("From jar: " + new JarredGreeting().getGreeting());
    LOG.info("From jar in dependency: " + new Vector2i(42, 73).toString());
  }
}
