package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import com.sudoplay.sudoext.meta.parser.AbstractMetaElementParser;
import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;
import org.json.JSONObject;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class VersionParser extends
    AbstractMetaElementParser {

  @Override
  public void parse(JSONObject jsonObject, Meta store) throws MetaParseException {
    store.setVersion(new DefaultArtifactVersion(this.readString("version", jsonObject)));
  }
}
