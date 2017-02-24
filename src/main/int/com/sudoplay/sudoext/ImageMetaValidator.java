package com.sudoplay.sudoext;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class ImageMetaValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(ImageMetaValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Container> containerList) {
    //return this.validateImage(path, meta.getImage());
    return false;
  }

  private boolean validateImage(Path path, String image) {
    path = path.resolve(image);

    if (!image.isEmpty() && !Files.exists(path)) {
      LOG.error("Missing image file [{}]", path);
      return false;
    }

    return true;
  }
}
