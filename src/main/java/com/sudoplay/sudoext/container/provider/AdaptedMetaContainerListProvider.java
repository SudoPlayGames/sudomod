package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.IMetaFactory;
import com.sudoplay.sudoext.meta.IMetaJsonProvider;
import com.sudoplay.sudoext.meta.parser.IMetaAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class AdaptedMetaContainerListProvider implements
    IContainerListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(AdaptedMetaContainerListProvider.class);

  private IContainerListProvider containerListProvider;
  private IMetaAdapter metaAdapter;
  private IMetaFactory metaFactory;
  private IMetaJsonProvider metaJsonProvider;

  public AdaptedMetaContainerListProvider(
      IContainerListProvider containerListProvider,
      IMetaAdapter metaAdapter,
      IMetaFactory metaFactory,
      IMetaJsonProvider metaJsonProvider
  ) {
    this.containerListProvider = containerListProvider;
    this.metaAdapter = metaAdapter;
    this.metaFactory = metaFactory;
    this.metaJsonProvider = metaJsonProvider;
  }

  @Override
  public List<Container> getContainerList() {
    LOG.debug("Entering getContainerList()");

    Path containerPath;
    JSONObject jsonObject;

    int metaFilesLoadedCount = 0;

    List<Container> containerList = this.containerListProvider
        .getContainerList();

    for (Container container : containerList) {
      containerPath = container.getPath();

      try {
        jsonObject = this.metaJsonProvider
            .load(containerPath);

      } catch (IOException e) {
        container.setValid(false);
        LOG.error(String.format("Unable to load meta file for container: %s", containerPath), e);
        continue;

      } catch (JSONException e) {
        container.setValid(false);
        LOG.error(String.format("Unable to parse meta file for container: %s", containerPath), e);
        continue;
      }

      try {
        container.setMeta(this.metaAdapter.adapt(jsonObject, this.metaFactory.create()));
        metaFilesLoadedCount += 1;

      } catch (Exception e) {
        container.setValid(false);
        LOG.error(String.format("Unable to adapt meta file for container: %s", containerPath), e);
      }
    }

    LOG.info("Loaded [{}] meta files", metaFilesLoadedCount);
    LOG.debug("Leaving load()");
    LOG.trace("...[{}]", containerList);
    return containerList;
  }
}
