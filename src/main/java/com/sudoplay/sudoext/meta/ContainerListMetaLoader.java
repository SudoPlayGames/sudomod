package com.sudoplay.sudoext.meta;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.parser.IMetaParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ContainerListMetaLoader implements IContainerListMetaLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ContainerListMetaLoader.class);

  private IMetaParser metaParser;
  private IMetaFactory metaFactory;
  private String metaFilename;

  public ContainerListMetaLoader(
      IMetaParser metaParser,
      IMetaFactory metaFactory,
      String metaFilename
  ) {
    this.metaParser = metaParser;
    this.metaFactory = metaFactory;
    this.metaFilename = metaFilename;
  }

  @Override
  public List<Container> load(
      List<Container> containerList,
      ArrayList<Container> store
  ) {
    LOG.debug("Entering load(containerList, store)");
    LOG.trace("...containerList=[{}]", containerList);
    LOG.trace("...store=[{}]", store);

    int count = 0;

    for (Container container : containerList) {
      Path location = container.getPath();
      Path metaFilePath = location.resolve(this.metaFilename);

      try {
        String metaJsonString = new String(Files.readAllBytes(metaFilePath));
        Meta meta = this.metaParser.parseMetaFile(metaJsonString, this.metaFactory.create());
        container.setMeta(meta);
        store.add(container);
        count += 1;

      } catch (Exception e) {
        LOG.error(String.format("Unable to parse meta file: %s", metaFilePath), e);
      }
    }

    LOG.info("Loaded [{}] meta files", count);
    LOG.debug("Leaving load()");
    LOG.trace("...[{}]", store);
    return store;
  }
}
