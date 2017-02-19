package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.parser.AbstractModInfoElementParser;
import com.sudoplay.sudomod.versioning.DefaultArtifactVersion;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class VersionParser extends
    AbstractModInfoElementParser {

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setVersion(new DefaultArtifactVersion(this.readString("version", jsonObject)));
  }
}
