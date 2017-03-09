package com.sudoplay.sudoxt.meta;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IJsonAdapter {

  JSONObject adapt(String jsonString) throws JSONException;
}
