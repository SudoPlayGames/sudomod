package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ContainerListValidator implements
    IContainerListValidator {

  private static final Logger LOG = LoggerFactory.getLogger(ContainerListValidator.class);

  private IMetaValidator metaValidator;

  public ContainerListValidator(IMetaValidator metaValidator) {
    this.metaValidator = metaValidator;
  }

  @Override
  public List<Container> validate(List<Container> containerList, List<Container> store) {
    LOG.debug("Entering validate(containerList, store)");
    LOG.trace("...containerList=[{}]", containerList);
    LOG.trace("...store=[{}]", store);

    Map<String, Container> validatedContainerMap = new HashMap<>();
    List<Container> invalidContainers = new ArrayList<>();

    for (Container container : containerList) {
      Path path = container.getPath();
      Meta meta = container.getMeta();

      if (this.metaValidator.isValid(meta, path, containerList)) {
        validatedContainerMap.put(meta.getId(), container);
        LOG.debug("Validated meta file for [{}]", meta.getId());

      } else {
        invalidContainers.add(container);
        LOG.error("Meta file for [{}] failed validation", meta.getId());
      }
    }

    // remove all plugins that are dependent on an invalid plugin
    for (Container container : invalidContainers) {
      this.removeDependents(container, validatedContainerMap);
    }

    store.addAll(validatedContainerMap.values());

    LOG.info("Validated meta files for [{}] plugins", validatedContainerMap.size());
    LOG.debug("Leaving validate()");
    LOG.trace("...[{}]", store);
    return store;
  }

  private void removeDependents(Container container, Map<String, Container> map) {
    Meta meta = container.getMeta();
    String id = meta.getId();
    List<Dependency> dependentList = meta.getDependencyContainer().getDependentList();

    for (Dependency dependency : dependentList) {
      Container dependentContainer;
      String dependencyId = dependency.getId();

      if ((dependentContainer = map.remove(dependencyId)) != null) {
        LOG.error("Dependent [{}] prevented from loading by invalid dependency [{}]", dependencyId, id);
        removeDependents(dependentContainer, map);
      }
    }
  }

}
