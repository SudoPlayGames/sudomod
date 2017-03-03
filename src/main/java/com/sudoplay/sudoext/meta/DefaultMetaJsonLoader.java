package com.sudoplay.sudoext.meta;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Provides the meta {@link JSONObject}.
 * <p>
 * Created by codetaylor on 3/1/2017.
 */
public class DefaultMetaJsonLoader implements
    IMetaJsonLoader {

  private IStringLoader stringLoader;
  private IJsonAdapter jsonAdapter;
  private String metaFilename;

  public DefaultMetaJsonLoader(
      IStringLoader stringLoader,
      IJsonAdapter jsonAdapter,
      String metaFilename
  ) {
    this.stringLoader = stringLoader;
    this.jsonAdapter = jsonAdapter;
    this.metaFilename = metaFilename;
  }

  @Override
  public JSONObject load(Path containerPath) throws IOException, JSONException {
    return this.jsonAdapter.adapt(this.stringLoader.load(containerPath.resolve(this.metaFilename)));
  }
}
