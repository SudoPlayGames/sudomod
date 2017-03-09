package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import com.sudoplay.sudoxt.versioning.ArtifactVersion;
import com.sudoplay.sudoxt.versioning.VersionRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class ApiVersionValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(ApiVersionValidator.class);

  private ArtifactVersion apiVersion;

  public ApiVersionValidator(ArtifactVersion apiVersion) {
    this.apiVersion = apiVersion;
  }

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {
    return this.validateApiVersion(meta.getApiVersionRange());
  }

  private boolean validateApiVersion(VersionRange apiVersionRange) {
    boolean containsVersion = apiVersionRange.containsVersion(this.apiVersion);

    if (!containsVersion) {
      LOG.error("Current api version [{}] falls outside of api version constraints [{}]", this.apiVersion,
          apiVersionRange);
      return false;
    }

    return true;
  }
}
