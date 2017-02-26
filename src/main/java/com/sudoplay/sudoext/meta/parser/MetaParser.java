package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import org.json.JSONObject;
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
  public Meta parseMetaFile(String jsonString, Meta store) throws MetaParseException {

    LOG.debug("Entering parseMetaFile(jsonString, store)");
    LOG.trace("...jsonString=[{}]", jsonString);
    LOG.trace("...store=[{}]", store);

    JSONObject jsonObject;

    try {
      jsonObject = new JSONObject(jsonString);

    } catch (Exception e) {
      throw new MetaParseException("Error parsing meta", e);
    }

    for (IMetaElementParser parser : this.elementParsers) {
      parser.parse(jsonObject, store);
    }

    LOG.debug("Leaving parseMetaFile()");
    LOG.trace("...[{}]", store);

    return store;
  }
}
