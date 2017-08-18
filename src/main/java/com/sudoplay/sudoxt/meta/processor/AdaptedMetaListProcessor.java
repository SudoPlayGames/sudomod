package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.ChainedMetaListProcessor;
import com.sudoplay.sudoxt.meta.IMetaJsonLoader;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class AdaptedMetaListProcessor extends
    ChainedMetaListProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(AdaptedMetaListProcessor.class);

  private IMetaAdapter metaAdapter;
  private IMetaJsonLoader metaJsonLoader;

  public AdaptedMetaListProcessor(
      ChainedMetaListProcessor metaListProcessor,
      IMetaAdapter metaAdapter,
      IMetaJsonLoader metaJsonLoader
  ) {
    super(metaListProcessor);
    this.metaAdapter = metaAdapter;
    this.metaJsonLoader = metaJsonLoader;
  }

  @Override
  public List<Meta> processLocal(List<Meta> metaList) {
    LOG.debug("Entering processLocal(metaList)");
    LOG.trace("...metaList=[{}]", metaList);

    Path path;
    JSONObject jsonObject;

    int metaFilesLoadedCount = 0;

    for (Meta meta : metaList) {
      path = meta.getPath();

      try {
        jsonObject = this.metaJsonLoader
            .load(path);

      } catch (IOException e) {
        meta.setValid(false);
        LOG.error(String.format("Unable to load meta file: %s", path), e);
        continue;

      } catch (JSONException e) {
        meta.setValid(false);
        LOG.error(String.format("Unable to parse meta file: %s", path), e);
        continue;
      }

      try {
        this.metaAdapter.adapt(jsonObject, meta);
        metaFilesLoadedCount += 1;

      } catch (Exception e) {
        meta.setValid(false);
        LOG.error(String.format("Unable to adapt meta file: %s", path), e);
      }
    }

    LOG.info("Loaded [{}] meta files", metaFilesLoadedCount);
    LOG.debug("Leaving processLocal()");
    LOG.trace("...[{}]", metaList);
    return metaList;
  }
}
