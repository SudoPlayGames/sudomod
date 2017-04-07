package com.sudoplay.sudoxt.api;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by codetaylor on 3/12/2017.
 */
public interface JsonObjectAPI {

  /**
   * Returns the number of name/value mappings in this object.
   */
  int length();

  /**
   * Maps {@code name} to {@code value}, overwriting any existing name/value
   * mapping with the same name.
   *
   * @return this object
   */
  JsonObjectAPI put(String name, String value);

  /**
   * Maps {@code name} to {@code value}, overwriting any existing name/value
   * mapping with the same name.
   *
   * @return this object
   */
  JsonObjectAPI put(String name, boolean value);

  /**
   * Maps {@code name} to {@code value}, overwriting any existing name/value
   * mapping with the same name.
   *
   * @param value a finite value. May not be {@link Float#isNaN() NaNs} or
   *              {@link Float#isInfinite() infinities}.
   * @return this object
   */
  JsonObjectAPI put(String name, float value);

  /**
   * Maps {@code name} to {@code value}, overwriting any existing name/value
   * mapping with the same name.
   *
   * @return this object
   */
  JsonObjectAPI put(String name, int value);

  /**
   * Maps {@code name} to {@code value}, overwriting any existing name/value
   * mapping with the same name.
   *
   * @return this object
   */
  JsonObjectAPI put(String name, JsonObjectAPI value);

  /**
   * Maps {@code name} to {@code value}, overwriting any existing name/value
   * mapping with the same name.
   *
   * @return this object
   */
  JsonObjectAPI put(String name, JsonArrayAPI value);

  /**
   * Removes the named mapping if it exists; does nothing otherwise.
   *
   * @return the value previously mapped by {@code name}, or null if there was
   * no such mapping.
   */
  Object remove(String name);

  /**
   * Returns true if this object is the null object.
   */
  boolean isNull();

  /**
   * Returns true if this object has no mapping for {@code name} or if it has
   * a mapping whose value is null.
   */
  boolean isNull(String name);

  /**
   * Returns true if this object has a mapping for {@code name}. The mapping
   * may be null.
   */
  boolean has(String name);

  /**
   * Returns the value mapped by {@code name} if it exists and is a boolean or
   * can be coerced to a boolean, or {@code fallback} otherwise.
   */
  boolean getBoolean(String name, boolean fallback);

  /**
   * Returns the value mapped by {@code name} if it exists and is a float or
   * can be coerced to a float, or {@code fallback} otherwise.
   */
  float getFloat(String name, float fallback);

  /**
   * Returns the value mapped by {@code name} if it exists and is an int or
   * can be coerced to an int, or {@code fallback} otherwise.
   */
  int getInt(String name, int fallback);

  /**
   * Returns the value mapped by {@code name} if it exists, coercing it if
   * necessary, or {@code fallback} if no such mapping exists.
   */
  String getString(String name, String fallback);

  /**
   * Returns the value mapped by {@code name} if it exists and is a {@code
   * JSONArray}, or null otherwise.
   */
  JsonArrayAPI getJSONArray(String name);

  /**
   * Returns the value mapped by {@code name} if it exists and is a {@code
   * JSONObject}, or null otherwise.
   */
  JsonObjectAPI getJSONObject(String name);

  /**
   * Returns an iterator of the {@code String} names in this object. The
   * returned iterator supports {@link Iterator#remove() remove}, which will
   * remove the corresponding mapping from this object. If this object is
   * modified after the iterator is returned, the iterator's behavior is
   * undefined. The order of the keys is undefined.
   */
  Iterator<String> keys();

  /**
   * Returns the set of {@code String} names in this object. The returned set
   * is a view of the keys in this object. {@link Set#remove(Object)} will remove
   * the corresponding mapping from this object and set iterator behaviour
   * is undefined if this object is modified after it is returned.
   * <p>
   * See {@link #keys()}.
   */
  Set<String> keySet();

  /**
   * Encodes this object as a human readable JSON string for debugging, such
   * as:
   * <pre>
   * {
   *     "query": "Pizza",
   *     "locations": [
   *         94043,
   *         90210
   *     ]
   * }</pre>
   *
   * @param indentSpaces the number of spaces to indent for each level of
   *                     nesting
   */
  String toString(int indentSpaces);
}
