package mod;

import com.sudoplay.math.Vector2i;
import com.sudoplay.sudoxt.api.*;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModPlugin implements
    Plugin {

  private static final LoggerAPI LOG = LoggingAPI.getLog(ModPlugin.class);

  @Override
  public void onGreeting() {
    //IGreeting greeting = new HelloUniverseGreeting();

    //LOG.info(greeting.getGreeting());

    LOG.info(new Vector2i(42, 73).toString());

    LOG.info("{}", TextIO.loadText("file.txt"));

    JsonObjectAPI json = JsonIO.loadJson("mod-info.json");

    if (json != null) {
      LOG.info("{}", json.toString(4));
    }
  }
}
