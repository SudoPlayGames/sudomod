package com.sudoplay.sudoext.meta.parser;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.sudoplay.sudoext.meta.InvalidMetaException;
import com.sudoplay.sudoext.meta.Meta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates the meta json string.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class MetaParser implements
    IMetaParser {

  private static final Logger LOG = LoggerFactory.getLogger(MetaParser.class);

  private IMetaElementParser[] elementParsers;

  public MetaParser(IMetaElementParser[] elementParsers) {
    this.elementParsers = elementParsers;
  }

  @Override
  public Meta parseMetaFile(String jsonString, Meta store) throws InvalidMetaException {

    LOG.debug("Entering parseMetaFile(jsonString, store)");
    LOG.trace("...jsonString=[{}]", jsonString);
    LOG.trace("...store=[{}]", store);

    JsonValue jsonValue;
    JsonObject jsonObject;

    try {
      jsonValue = Json.parse(jsonString);

    } catch (Exception e) {
      throw new InvalidMetaException("Error parsing meta", e);
    }

    if (!jsonValue.isObject()) {
      throw new InvalidMetaException(String.format("Expected meta object, got: %s", jsonValue));
    }

    jsonObject = jsonValue.asObject();

    for (IMetaElementParser parser : this.elementParsers) {
      parser.parse(jsonObject, store);
    }

    LOG.debug("Leaving parseMetaFile()");
    LOG.trace("...[{}]", store);

    return store;
  }
}
