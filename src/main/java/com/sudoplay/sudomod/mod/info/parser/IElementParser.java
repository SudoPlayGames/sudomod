package com.sudoplay.sudomod.mod.info.parser;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IElementParser {

  void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException;

}
