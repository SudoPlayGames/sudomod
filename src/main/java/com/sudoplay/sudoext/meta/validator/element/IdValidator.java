package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class IdValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(IdValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Container> containerList) {

    String id = meta.getId();

    if (id == null || id.isEmpty()) {
      LOG.error("Meta [id] is invalid: %s; must not be empty or null, valid characters are a-z 0-9 - _", id);
      return false;
    }

    if (id.replaceAll("[a-z0-9-_]", "").length() > 0) {
      LOG.error("Meta [id] is invalid: %s; must not be empty or null, valid characters are a-z 0-9 - _", id);
      return false;
    }

    int idCount = 0;

    for (Container container : containerList) {

      if (id.equals(container.getMeta().getId())) {
        idCount += 1;
      }
    }

    boolean uniqueId = (idCount == 1);

    if (!uniqueId) {
      LOG.error("Duplicate id found [{}], ids must be unique", id);
    }

    return uniqueId;

  }

}
