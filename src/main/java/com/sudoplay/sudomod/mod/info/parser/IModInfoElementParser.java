package com.sudoplay.sudomod.mod.info.parser;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;

/**
 * Created by sk3lls on 2/18/2017.
 */
public interface IModInfoElementParser {

  void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException;

}
