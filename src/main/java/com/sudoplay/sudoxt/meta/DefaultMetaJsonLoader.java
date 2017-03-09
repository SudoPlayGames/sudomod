package com.sudoplay.sudoxt.meta;

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

  public DefaultMetaJsonLoader(
      IStringLoader stringLoader,
      IJsonAdapter jsonAdapter
  ) {
    this.stringLoader = stringLoader;
    this.jsonAdapter = jsonAdapter;
  }

  @Override
  public JSONObject load(Path jsonPath) throws IOException, JSONException {
    return this.jsonAdapter.adapt(this.stringLoader.load(jsonPath));
  }
}
