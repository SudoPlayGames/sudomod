package com.sudoplay.sudoxt.meta;

import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IMetaJsonLoader {

  JSONObject load(Path containerPath) throws IOException, JSONException;
}
