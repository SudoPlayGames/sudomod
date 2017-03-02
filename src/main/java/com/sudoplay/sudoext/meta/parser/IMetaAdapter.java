package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaAdaptException;
import org.json.JSONObject;

/**
 * Adapts the meta jsonObject.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaAdapter {

  /**
   * Adapts the provided jsonObject to a meta object.
   *
   * @param jsonObject json object
   * @param meta       meta store
   * @return Meta object
   * @throws MetaAdaptException if any errors occur during parsing
   */
  Meta adapt(JSONObject jsonObject, Meta meta) throws MetaAdaptException;
}
