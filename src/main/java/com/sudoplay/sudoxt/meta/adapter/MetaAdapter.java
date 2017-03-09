package com.sudoplay.sudoxt.meta.adapter;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatches to each registered adapter for the given {@link JSONObject} and {@link Meta}.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class MetaAdapter implements
    IMetaAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(MetaAdapter.class);

  private IMetaAdapter[] elementParsers;

  public MetaAdapter(IMetaAdapter[] elementParsers) {
    this.elementParsers = elementParsers;
  }

  @Override
  public void adapt(JSONObject jsonObject, Meta meta) throws MetaAdaptException {

    LOG.debug("Entering adapt(jsonString, meta)");
    LOG.trace("...jsonString=[{}]", jsonObject);
    LOG.trace("...meta=[{}]", meta);

    List<Throwable> throwableList;

    throwableList = new ArrayList<>();

    for (IMetaAdapter adapter : this.elementParsers) {

      try {
        //noinspection unchecked
        adapter.adapt(jsonObject, meta);

      } catch (Exception e) {
        throwableList.add(e);
      }
    }

    if (!throwableList.isEmpty()) {

      for (Throwable throwable : throwableList) {
        LOG.error(throwable.getMessage());
      }
      throw new MetaAdaptException(String.format("Caught [%d] error(s) adapting meta", throwableList.size()));
    }

    LOG.debug("Leaving adapt()");
  }
}
