package com.sudoplay.sudomod.mod.info.parser.element;

import com.eclipsesource.json.JsonObject;
import com.sudoplay.sudomod.mod.info.InvalidModInfoException;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.parser.AbstractModInfoElementParser;

import java.io.File;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ImageParser extends
    AbstractModInfoElementParser {

  @Override
  public void parse(JsonObject jsonObject, ModInfo store) throws InvalidModInfoException {
    store.setImage(this.readImageFile("image", jsonObject));
  }

  private File readImageFile(String key, JsonObject jsonObject) throws InvalidModInfoException {
    String imageFileString = this.readOptionalString(key, jsonObject);

    if (imageFileString.isEmpty()) {
      return null;
    }

    return new File(imageFileString);
  }
}
