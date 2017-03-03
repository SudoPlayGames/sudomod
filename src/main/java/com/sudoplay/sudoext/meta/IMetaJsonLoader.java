package com.sudoplay.sudoext.meta;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IMetaJsonLoader {

  JSONObject load(Path containerPath) throws IOException, JSONException;
}
