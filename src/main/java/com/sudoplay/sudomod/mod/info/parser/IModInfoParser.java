package com.sudoplay.sudomod.mod.info.parser;

import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;

/**
 * Validates the mod-info.json file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IModInfoParser {

  /**
   * Parses the provided mod info string as json and returns a {@link ModInfo} object containing the parsed data.
   *
   * @param modInfoJson json string
   * @return ModInfo object
   * @throws InvalidModInfoException if any errors occur during parsing
   */
  ModInfo parseModInfoFile(String modInfoJson, ModInfo store) throws InvalidModInfoException;

}
