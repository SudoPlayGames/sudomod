package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
    List<Throwable> throwableList;

    try {
      jsonObject = new JSONObject(jsonString);

    } catch (Exception e) {
      throw new MetaParseException("Error parsing meta", e);
    }

    throwableList = new ArrayList<>();

    for (IMetaElementParser parser : this.elementParsers) {

      try {
        parser.parse(jsonObject, store);

      } catch (Exception e) {
        throwableList.add(e);
      }
    }

    if (!throwableList.isEmpty()) {

      for (Throwable throwable : throwableList) {
        LOG.error(throwable.getMessage());
      }
      throw new MetaParseException(String.format("Caught [%d] error(s) parsing meta", throwableList.size()));
    }

    LOG.debug("Leaving parseMetaFile()");
    LOG.trace("...[{}]", store);

    return store;
  }
}
