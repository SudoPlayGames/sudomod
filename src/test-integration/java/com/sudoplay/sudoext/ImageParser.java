package com.sudoplay.sudoext;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.parser.AbstractMetaElementParser;
import org.json.JSONObject;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ImageParser extends
    AbstractMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    //store.setImage(this.readOptionalString("image", jsonObject));
  }
}
