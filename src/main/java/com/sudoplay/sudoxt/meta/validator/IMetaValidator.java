package com.sudoplay.sudoxt.meta.validator;

import com.sudoplay.sudoxt.meta.Meta;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IMetaValidator {

  boolean isValid(Meta meta, Path path, List<Meta> metaList);
}
