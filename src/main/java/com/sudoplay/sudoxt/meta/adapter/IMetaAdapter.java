package com.sudoplay.sudoxt.meta.adapter;

import com.sudoplay.sudoxt.meta.Meta;
import org.json.JSONObject;

/**
 * Adapts individual elements to the provided meta object.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaAdapter {

  void adapt(JSONObject jsonObject, Meta meta) throws Exception;
}
