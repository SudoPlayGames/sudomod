package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.parser.AbstractModInfoElementParser;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class IdParser extends
    AbstractModInfoElementParser {

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setId(this.readString("id", jsonObject));
  }
}
