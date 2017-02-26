package com.sudoplay.sudoext.meta.parser;

import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.Meta;

/**
 * Validates the meta json string.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IMetaParser {

  /**
   * Parses the provided meta string as json and returns a {@link Meta} object containing the parsed data.
   *
   * @param jsonString json string
   * @return Meta object
   * @throws MetaParseException if any errors occur during parsing
   */
  Meta parseMetaFile(String jsonString, Meta store) throws MetaParseException;

}
