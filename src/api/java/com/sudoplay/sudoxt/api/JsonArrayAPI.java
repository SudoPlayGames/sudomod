package com.sudoplay.sudoxt.api;

/**
 * Created by codetaylor on 3/12/2017.
 */
public interface JsonArrayAPI {

  /**
   * Returns the number of values in this array.
   */
  int length();

  /**
   * Appends {@code value} to the end of this array.
   *
   * @return this array.
   */
  JsonArrayAPI add(String value);

  /**
   * Appends {@code value} to the end of this array.
   *
   * @return this array.
   */
  JsonArrayAPI add(boolean value);

  /**
   * Appends {@code value} to the end of this array.
   *
   * @param value a finite value. May not be {@link Float#isNaN() NaNs} or
   *              {@link Float#isInfinite() infinities}.
   * @return this array.
   */
  JsonArrayAPI add(float value);

  /**
   * Appends {@code value} to the end of this array.
   *
   * @return this array.
   */
  JsonArrayAPI add(int value);

  /**
   * Appends {@code value} to the end of this array.
   *
   * @return this array.
   */
  JsonArrayAPI add(JsonObjectAPI value);

  /**
   * Appends {@code value} to the end of this array.
   *
   * @return this array.
   */
  JsonArrayAPI add(JsonArrayAPI value);

  /**
   * Sets the value at {@code index} to {@code value}, null padding this array
   * to the required length if necessary. If a value already exists at {@code
   * index}, it will be replaced.
   *
   * @return this array.
   */
  JsonArrayAPI set(int index, String value);

  /**
   * Sets the value at {@code index} to {@code value}, null padding this array
   * to the required length if necessary. If a value already exists at {@code
   * index}, it will be replaced.
   *
   * @return this array.
   */
  JsonArrayAPI set(int index, boolean value);

  /**
   * Sets the value at {@code index} to {@code value}, null padding this array
   * to the required length if necessary. If a value already exists at {@code
   * index}, it will be replaced.
   *
   * @param value a finite value. May not be {@link Float#isNaN() NaNs} or
   *              {@link Float#isInfinite() infinities}.
   * @return this array.
   */
  JsonArrayAPI set(int index, float value);

  /**
   * Sets the value at {@code index} to {@code value}, null padding this array
   * to the required length if necessary. If a value already exists at {@code
   * index}, it will be replaced.
   *
   * @return this array.
   */
  JsonArrayAPI set(int index, int value);

  /**
   * Sets the value at {@code index} to {@code value}, null padding this array
   * to the required length if necessary. If a value already exists at {@code
   * index}, it will be replaced.
   *
   * @return this array.
   */
  JsonArrayAPI set(int index, JsonObjectAPI value);

  /**
   * Sets the value at {@code index} to {@code value}, null padding this array
   * to the required length if necessary. If a value already exists at {@code
   * index}, it will be replaced.
   *
   * @return this array.
   */
  JsonArrayAPI set(int index, JsonArrayAPI value);

  /**
   * Returns true if this array has no value at {@code index}, or if its value
   * is the {@code null} reference.
   */
  boolean isNull(int index);

  /**
   * Returns the value at {@code index} if it exists and is a string or can
   * be coerced to a string. Returns {@code fallback} otherwise.
   */
  String getString(int index, String fallback);

  /**
   * Returns the value at {@code index} if it exists and is a boolean or can
   * be coerced to a boolean. Returns {@code fallback} otherwise.
   */
  boolean getBoolean(int index, boolean fallback);

  /**
   * Returns the value at {@code index} if it exists and is a float or can
   * be coerced to a float. Returns {@code fallback} otherwise.
   */
  float getFloat(int index, float fallback);

  /**
   * Returns the value at {@code index} if it exists and is an int or
   * can be coerced to an int. Returns {@code fallback} otherwise.
   */
  int getInt(int index, int fallback);

  /**
   * Returns the value at {@code index} if it exists and is a {@code
   * JSONArray}. Returns null otherwise.
   */
  JsonArrayAPI getJSONArray(int index);

  /**
   * Returns the value at {@code index} if it exists and is a {@code
   * JSONObject}. Returns null otherwise.
   */
  JsonObjectAPI getJSONObject(int index);

  /**
   * Encodes this array as a human readable JSON string for debugging, such
   * as:
   * <pre>
   * [
   *     94043,
   *     90210
   * ]</pre>
   *
   * @param indentSpaces the number of spaces to indent for each level of
   *                     nesting.
   */
  String toString(int indentSpaces);
}
