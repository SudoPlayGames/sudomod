package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.parser.AbstractElementParser;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModPluginParser extends
    AbstractElementParser {

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setModPlugin(
        this.readString("mod-plugin", jsonObject)
            .replaceAll("\\\\", "/")
            .replaceAll("\\.java", "")
            .replaceAll("\\.class", "")
    );
  }
}
