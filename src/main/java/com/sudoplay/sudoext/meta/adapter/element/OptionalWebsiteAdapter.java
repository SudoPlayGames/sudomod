package com.sudoplay.sudoext.meta.adapter.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaAdaptException;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import org.json.JSONObject;

/**
 * Reads website form meta file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class OptionalWebsiteAdapter implements
    IMetaAdapter {

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws MetaAdaptException {
    meta.setWebsite(jsonObject.optString("website", ""));
  }
}
