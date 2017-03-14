package mod;

import com.sudoplay.sudoxt.api.LoggerAPI;
import com.sudoplay.sudoxt.api.LoggingAPI;
import com.sudoplay.sudoxt.api.Plugin;
import com.sudoplay.sudoxt.api.TextIO;
import mod.nested.HelloWorldGreeting;
import mod.nested.IGreeting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModPlugin implements
    Plugin {

  private static final LoggerAPI LOG = LoggingAPI.getLog(ModPlugin.class);

  private int testInt;
  private long[] testLongArray;

  @Override
  public void onGreeting() {

    IGreeting greeting = new HelloWorldGreeting();
    LOG.info(greeting.getGreeting());

    // good
    // ACE
    // System.exit(-1);

    List list = new ArrayList();
    for (int i = 0; i < 10000; i++) {
      list.add(i);
    }

    LOG.info("s=[{}]", list.size());

    LOG.info("{}", TextIO.loadText("../test-mod-b/file.txt"));
  }
}
