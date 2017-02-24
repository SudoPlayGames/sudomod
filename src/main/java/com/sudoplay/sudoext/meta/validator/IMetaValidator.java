package com.sudoplay.sudoext.meta.validator;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Meta;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IMetaValidator {
  boolean isValid(Meta meta, Path path, List<Container> containerList);
}
