package com.sudoplay.sudoxt.api;

import com.sudoplay.sudoxt.classloader.intercept.InjectStaticField;
import com.sudoplay.sudoxt.classloader.intercept.InterceptClass;
import com.sudoplay.sudoxt.container.SandboxPathProvider;
import com.sudoplay.sudoxt.meta.DefaultJsonAdapter;
import com.sudoplay.sudoxt.meta.DefaultStringLoader;
import org.json.JSONObject;

import java.nio.charset.Charset;

/**
 * Created by codetaylor on 3/7/2017.
 */
@InterceptClass
public class JsonIO {

  private static final LoggerAPI LOG = LoggingAPI.getLog(JsonIO.class);

  @InjectStaticField
  private static SandboxPathProvider SANDBOX_PATH_PROVIDER;

  @InjectStaticField
  private static IJsonObjectAPIProvider JSON_OBJECT_API_PROVIDER;

  public static JsonObjectAPI loadJson(String path) {

    try {
      Charset charset = Charset.forName("UTF-8");
      DefaultStringLoader stringLoader = new DefaultStringLoader(charset);
      String jsonString = stringLoader.load(SANDBOX_PATH_PROVIDER.getPath(path));
      JSONObject jsonObject = new DefaultJsonAdapter().adapt(jsonString);
      return JSON_OBJECT_API_PROVIDER.getJsonObjectAPI(jsonObject);

    } catch (Exception e) {
      LOG.error(e.getClass().getSimpleName() + ": " + e.getMessage());
      LOG.debug("", e);
    }
    return null;
  }

  private JsonIO() {
    //
  }
}
