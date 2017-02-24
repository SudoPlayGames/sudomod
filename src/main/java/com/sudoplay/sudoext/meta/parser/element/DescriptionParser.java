package com.sudoplay.sudoext.meta.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudoext.meta.InvalidMetaException;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.AbstractElementParser;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class DescriptionParser extends
    AbstractElementParser {

  @Override
  public void parse(JsonObject jsonObject, Meta store) throws InvalidMetaException {
    store.setDescription(this.readString("description", jsonObject));
  }
}
