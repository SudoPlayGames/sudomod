package com.sudoplay.sudoext.meta.adapter;

import com.sudoplay.sudoext.meta.Meta;
import org.json.JSONObject;

/**
 * Adapts individual elements to the provided meta object.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaAdapter {

  void adapt(JSONObject jsonObject, Meta meta) throws Exception;
}
