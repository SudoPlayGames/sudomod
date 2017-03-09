package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.ChainedMetaListProcessor;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ValidatedMetaListProcessor extends
    ChainedMetaListProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(ValidatedMetaListProcessor.class);

  private IMetaValidator metaValidator;

  public ValidatedMetaListProcessor(
      ChainedMetaListProcessor metaListProcessor,
      IMetaValidator metaValidator
  ) {
    super(metaListProcessor);
    this.metaValidator = metaValidator;
  }

  @Override
  public List<Meta> processLocal(List<Meta> metaList) {
    LOG.debug("Entering processLocal(metaList)");
    LOG.trace("...metaList=[{}]", metaList);

    int validMetaCount = 0;

    for (Meta meta : metaList) {
      Path path = meta.getParentPath();

      if (this.metaValidator.isValid(meta, path, metaList)) {
        validMetaCount += 1;
        LOG.debug("Validated meta file for [{}]", meta.getId());

      } else {
        meta.setValid(false);
        LOG.error("Meta file for [{}] failed validation", meta.getId());
      }
    }

    LOG.info("Validated [{}] meta files", validMetaCount);
    LOG.debug("Leaving processLocal()");
    LOG.trace("...[{}]", metaList);
    return metaList;
  }
}
