package com.sudoplay.sudoxt.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

import static com.sudoplay.sudoxt.api.PreCondition.notNull;

/**
 * Created by codetaylor on 3/12/2017.
 */
public class JsonObjectProxy implements
    JsonObjectAPI {

  private static final Logger LOG = LoggerFactory.getLogger(JsonObjectProxy.class);

  private JSONObject jsonObject;

  public JsonObjectProxy(JSONObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  @Override
  public int length() {
    return this.jsonObject.length();
  }

  @Override
  public JsonObjectAPI put(String name, String value) {

    try {
      this.jsonObject.put(notNull(name), notNull(value));

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonObjectAPI put(String name, boolean value) {

    try {
      this.jsonObject.put(notNull(name), notNull(value));

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonObjectAPI put(String name, float value) {

    try {
      this.jsonObject.put(notNull(name), notNull(value));

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonObjectAPI put(String name, int value) {

    try {
      this.jsonObject.put(notNull(name), notNull(value));

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonObjectAPI put(String name, JsonObjectAPI value) {

    try {
      this.jsonObject.put(notNull(name), notNull(value));

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public JsonObjectAPI put(String name, JsonArrayAPI value) {

    try {
      this.jsonObject.put(notNull(name), notNull(value));

    } catch (JSONException e) {
      this.log(e);
    }
    return this;
  }

  @Override
  public Object remove(String name) {
    return this.jsonObject.remove(notNull(name));
  }

  @Override
  public boolean isNull() {
    return this.jsonObject == JSONObject.NULL;
  }

  @Override
  public boolean isNull(String name) {
    return this.jsonObject.isNull(notNull(name));
  }

  @Override
  public boolean has(String name) {
    return this.jsonObject.has(notNull(name));
  }

  @Override
  public boolean getBoolean(String name, boolean fallback) {
    return this.jsonObject.optBoolean(notNull(name), fallback);
  }

  @Override
  public float getFloat(String name, float fallback) {
    return (float) this.jsonObject.optDouble(notNull(name), fallback);
  }

  @Override
  public int getInt(String name, int fallback) {
    return this.jsonObject.optInt(notNull(name), fallback);
  }

  @Override
  public String getString(String name, String fallback) {
    return this.jsonObject.optString(notNull(name), fallback);
  }

  @Override
  public JsonArrayAPI getJSONArray(String name) {
    JSONArray jsonArray = this.jsonObject.optJSONArray(name);

    if (jsonArray == null) {
      return null;
    }
    return new JsonArrayProxy(jsonArray);
  }

  @Override
  public JsonObjectAPI getJSONObject(String name) {
    JSONObject jsonObject = this.jsonObject.optJSONObject(name);

    if (jsonObject == null) {
      return null;
    }
    return new JsonObjectProxy(jsonObject);
  }

  @Override
  public Iterator<String> keys() {
    return this.jsonObject.keys();
  }

  @Override
  public Set<String> keySet() {
    return this.jsonObject.keySet();
  }

  @Override
  public String toString(int indentSpaces) {

    try {
      return this.jsonObject.toString(indentSpaces);

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
