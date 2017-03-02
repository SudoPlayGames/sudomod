package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.Meta;
import org.json.JSONObject;

/**
 * Adapts individual elements to the provided meta object.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaElementAdapter<M extends Meta> {

  void adapt(JSONObject jsonObject, M meta) throws Exception;
}
