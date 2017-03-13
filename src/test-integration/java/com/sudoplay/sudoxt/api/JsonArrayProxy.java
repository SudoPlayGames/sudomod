package com.sudoplay.sudoxt.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sudoplay.sudoxt.api.PreCondition.notNegative;

/**
 * Created by codetaylor on 3/12/2017.
 */
public class JsonArrayProxy implements
    JsonArrayAPI {

  private static final Logger LOG = LoggerFactory.getLogger(JsonArrayProxy.class);

  private JSONArray jsonArray;

  public JsonArrayProxy(JSONArray jsonArray) {
    this.jsonArray = jsonArray;
  }

  @Override
  public int length() {
    return this.jsonArray.length();
  }

  @Override
  public JsonArrayAPI add(String value) {
    this.jsonArray.put(value);
    return this;
  }

  @Override
  public JsonArrayAPI add(boolean value) {
    this.jsonArray.put(value);
    return this;
  }

  @Override
  public JsonArrayAPI add(float value) {

    try {
      this.jsonArray.put(value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonArrayAPI add(int value) {
    this.jsonArray.put(value);
    return this;
  }

  @Override
  public JsonArrayAPI add(JsonObjectAPI value) {
    this.jsonArray.put(value);
    return this;
  }

  @Override
  public JsonArrayAPI add(JsonArrayAPI value) {
    this.jsonArray.put(value);
    return this;
  }

  @Override
  public JsonArrayAPI set(int index, String value) {

    try {
      this.jsonArray.put(notNegative(index), value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonArrayAPI set(int index, boolean value) {
    try {
      this.jsonArray.put(notNegative(index), value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonArrayAPI set(int index, float value) {

    try {
      this.jsonArray.put(notNegative(index), value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonArrayAPI set(int index, int value) {

    try {
      this.jsonArray.put(notNegative(index), value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonArrayAPI set(int index, JsonObjectAPI value) {

    try {
      this.jsonArray.put(notNegative(index), value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonArrayAPI set(int index, JsonArrayAPI value) {

    try {
      this.jsonArray.put(notNegative(index), value);

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public boolean isNull(int index) {
    return this.jsonArray.isNull(notNegative(index));
  }

  @Override
  public String getString(int index, String fallback) {
    return this.jsonArray.optString(notNegative(index), fallback);
  }

  @Override
  public boolean getBoolean(int index, boolean fallback) {
    return this.jsonArray.optBoolean(notNegative(index), fallback);
  }

  @Override
  public float getFloat(int index, float fallback) {
    return (float) this.jsonArray.optDouble(notNegative(index), fallback);
  }

  @Override
  public int getInt(int index, int fallback) {
    return this.jsonArray.optInt(notNegative(index), fallback);
  }

  @Override
  public JsonArrayAPI getJSONArray(int index) {
    JSONArray jsonArray = this.jsonArray.optJSONArray(notNegative(index));

    if (jsonArray == null) {
      return null;
    }
    return new JsonArrayProxy(jsonArray);
  }

  @Override
  public JsonObjectAPI getJSONObject(int index) {
    JSONObject jsonObject = this.jsonArray.optJSONObject(notNegative(index));

    if (jsonObject == null) {
      return null;
    }
    return new JsonObjectProxy(jsonObject);
  }

  @Override
  public String toString(int indentSpaces) {

    try {
      return this.jsonArray.toString(indentSpaces);

    } catch (JSONException e) {
      this.log(e);
    }
    return null;
  }

  private void log(JSONException e) {
    LOG.error("[{}]: {}", e.getClass().getSimpleName(), e.getMessage());
    LOG.debug("", e);
  }
}
