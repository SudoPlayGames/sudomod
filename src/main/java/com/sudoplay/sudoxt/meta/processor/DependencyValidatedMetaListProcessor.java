package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.ChainedMetaListProcessor;
import com.sudoplay.sudoxt.meta.Dependency;
import com.sudoplay.sudoxt.meta.Meta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class DependencyValidatedMetaListProcessor extends
    ChainedMetaListProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(DependencyValidatedMetaListProcessor.class);

  public DependencyValidatedMetaListProcessor(
      ChainedMetaListProcessor metaListProcessor
  ) {
    super(metaListProcessor);
  }

  @Override
  public List<Meta> processLocal(List<Meta> metaList) {
    LOG.debug("Entering processLocal(metaList)");
    LOG.trace("...metaList=[{}]", metaList);

    Map<String, Meta> metaMap;

    metaMap = new HashMap<>();

    // build an id lookup map for all containers
    for (Meta meta : metaList) {
      metaMap.put(meta.getId(), meta);
    }

    // expects incoming list to be sorted in dependency order
    // if valid meta V requires any invalid meta, invalidate meta V

    metaList
        .stream()
        .filter(Meta::isValid)
        .forEach(meta -> {
          String metaId;
          Set<Dependency> requiredSet;

          metaId = meta.getId();
          requiredSet = meta
              .getRequiredDependencySet();

          for (Dependency dependency : requiredSet) {
            String dependencyId = dependency.getId();

            if (!metaMap.get(dependencyId).isValid()) {
              meta.setValid(false);
              LOG.error(
                  "Valid [{}] has required dependency on invalid [{}], flagging as invalid",
                  metaId,
                  dependencyId
              );
              break;
            }
          }
        });

    LOG.debug("Leaving processLocal()");
    LOG.trace("...[{}]", metaList);
    return metaList;
  }
}
