package testapi;

import com.sudoplay.sudoxt.classloader.intercept.InjectStaticField;
import com.sudoplay.sudoxt.classloader.intercept.InterceptClass;
import com.sudoplay.sudoxt.container.SandboxPathProvider;
import com.sudoplay.sudoxt.meta.DefaultStringLoader;

import java.nio.charset.Charset;

/**
 * Created by codetaylor on 3/7/2017.
 */
@InterceptClass
public class TextIO {

  private static final LoggerAPI LOG = LoggingAPI.getLog(TextIO.class);

  @InjectStaticField
  private static SandboxPathProvider SANDBOX_PATH_PROVIDER;

  public static String loadText(String path) {

    try {
      Charset charset = Charset.forName("UTF-8");
      return new DefaultStringLoader(charset).load(SANDBOX_PATH_PROVIDER.getPath(path));

    } catch (Exception e) {
      LOG.error(e.getClass().getSimpleName() + ": " + e.getMessage());
      LOG.trace("", e);
    }
    return null;
  }

  private TextIO() {
    //
  }
}
