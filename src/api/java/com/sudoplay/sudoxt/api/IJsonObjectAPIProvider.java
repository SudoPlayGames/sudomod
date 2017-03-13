package com.sudoplay.sudoxt.api;

import org.json.JSONObject;

/**
 * Created by codetaylor on 3/12/2017.
 */
public interface IJsonObjectAPIProvider {

  JsonObjectAPI getJsonObjectAPI(JSONObject jsonObject);
}
