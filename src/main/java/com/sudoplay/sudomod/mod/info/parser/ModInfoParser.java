package com.sudoplay.sudomod.mod.info.parser;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates the mod-info.json file.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class ModInfoParser implements
    IModInfoParser {

  private static final Logger LOG = LoggerFactory.getLogger(ModInfoParser.class);

  private IElementParser[] elementParsers;

  public ModInfoParser(IElementParser[] elementParsers) {
    this.elementParsers = elementParsers;
  }

  @Override
  public ModInfo parseModInfoFile(String modInfoJson, ModInfo store) throws InvalidModInfoException {

    LOG.debug("Entering parseModInfoFile(modInfoJson=[{}], store=[{}])", modInfoJson, store);

    JsonValue jsonValue;
    JsonObject jsonObject;

    try {
      jsonValue = Json.parse(modInfoJson);

    } catch (Exception e) {
      throw new InvalidModInfoException("Error parsing mod info", e);
    }

    if (!jsonValue.isObject()) {
      throw new InvalidModInfoException(String.format("Expected mod info object, got: %s", jsonValue));
    }

    jsonObject = jsonValue.asObject();

    for (IElementParser parser : this.elementParsers) {
      parser.parse(jsonObject, store);
    }

    LOG.debug("Leaving parseModInfoFile(): {}", store);

    return store;
  }
}
