package com.sudoplay.sudoext.meta.validator;

import com.sudoplay.sudoext.meta.Meta;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class MetaValidator implements IMetaValidator {

  private IMetaValidator[] metaValidators;

  public MetaValidator(IMetaValidator[] metaValidators) {
    this.metaValidators = metaValidators;
  }

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {

    boolean isValid = true;

    for (IMetaValidator validator : this.metaValidators) {
      isValid = validator.isValid(meta, path, metaList) && isValid;
    }

    return isValid;
  }

}
