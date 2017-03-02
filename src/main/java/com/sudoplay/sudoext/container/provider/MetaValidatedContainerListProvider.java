package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class MetaValidatedContainerListProvider implements
    IContainerListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(MetaValidatedContainerListProvider.class);

  private IContainerListProvider containerListProvider;
  private IMetaValidator metaValidator;

  public MetaValidatedContainerListProvider(
      IContainerListProvider containerListProvider,
      IMetaValidator metaValidator
  ) {
    this.containerListProvider = containerListProvider;
    this.metaValidator = metaValidator;
  }

  @Override
  public List<Container> getContainerList() {
    LOG.debug("Entering getContainerList()");

    int validContainerCount = 0;

    List<Container> containerList = this.containerListProvider
        .getContainerList();

    for (Container container : containerList) {
      Path path = container.getPath();
      Meta meta = container.getMeta();

      if (this.metaValidator.isValid(meta, path, containerList)) {
        container.setValid(true);
        validContainerCount += 1;
        LOG.debug("Validated meta file for [{}]", meta.getId());

      } else {
        container.setValid(false);
        LOG.error("Meta file for [{}] failed validation", meta.getId());
      }
    }

    LOG.info("Validated meta files for [{}] containers", validContainerCount);
    LOG.debug("Leaving validate()");
    LOG.trace("...[{}]", containerList);
    return containerList;
  }
}
