package com.sudoplay.sudoxt.api;

import org.json.JSONObject;

/**
 * Created by codetaylor on 3/12/2017.
 */
public class JsonObjectAPIProvider implements
    IJsonObjectAPIProvider {

  @Override
  public JsonObjectAPI getJsonObjectAPI(JSONObject jsonObject) {
    return new JsonObjectProxy(jsonObject);
  }
}
