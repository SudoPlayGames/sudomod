package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;

/**
 * Reads id from meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class IdAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws JSONException {
    meta.setId(jsonObject.getString("id"));
  }
}
