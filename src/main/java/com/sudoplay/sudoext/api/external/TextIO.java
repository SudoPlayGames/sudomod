package com.sudoplay.sudoext.api.external;

import com.sudoplay.sudoext.api.internal.PathProvider;
import com.sudoplay.sudoext.classloader.intercept.InjectStaticField;
import com.sudoplay.sudoext.classloader.intercept.InterceptClass;
import com.sudoplay.sudoext.meta.DefaultStringLoader;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by codetaylor on 3/7/2017.
 */
@InterceptClass
public class TextIO {

  private static final LogAPI LOG = LoggingAPI.getLog(TextIO.class);

  @InjectStaticField
  private static PathProvider PATH_PROVIDER;

  public static String loadText(String path) {
    try {
      Charset charset = Charset.forName("UTF-8");
      return new DefaultStringLoader(charset).load(PATH_PROVIDER.getPath(path));

    } catch (Exception e) {
      LOG.error(e.getClass().getSimpleName() + ": " + e.getMessage());
      LOG.debug("", e);
    }
    return null;
  }

  private TextIO() {
    //
  }
}
