package com.sudoplay.sudoext;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import org.json.JSONObject;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class PluginParser extends
    AbstractMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    /*store.setPlugin(
        this.readString("plugin", jsonObject)
            .replaceAll("\\\\", "/")
            .replaceAll("\\.java", "")
            .replaceAll("\\.class", "")
    );*/
  }
}
