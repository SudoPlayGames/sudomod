package mod;

import com.sudoplay.sudoxt.api.AncillaryPlugin;
import com.sudoplay.sudoxt.api.LoggerAPI;
import com.sudoplay.sudoxt.api.LoggingAPI;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class BlueAncillaryPlugin implements
    AncillaryPlugin {

  private static final LoggerAPI LOG = LoggingAPI.getLog(BlueAncillaryPlugin.class);

  @Override
  public void doStuff() {
    LOG.info("I am Blue!");
  }

  @Override
  public int addValues(int a, int b) {
    return a + b;
  }
}
