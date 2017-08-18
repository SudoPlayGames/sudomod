package com.sudoplay.sudoxt.meta;

import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IJsonAdapter {

  JSONObject adapt(String jsonString) throws JSONException;
}
