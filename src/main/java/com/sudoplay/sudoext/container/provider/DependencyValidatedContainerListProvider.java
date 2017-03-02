package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class DependencyValidatedContainerListProvider implements
    IContainerListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(DependencyValidatedContainerListProvider.class);

  private IContainerListProvider containerListProvider;

  public DependencyValidatedContainerListProvider(IContainerListProvider containerListProvider) {
    this.containerListProvider = containerListProvider;
  }

  @Override
  public List<Container> getContainerList() {
    LOG.debug("Entering getContainerList()");

    Map<String, Container> containerMap;
    List<Container> containerList;

    containerMap = new HashMap<>();
    containerList = this.containerListProvider
        .getContainerList();

    // build an id lookup map for all containers
    for (Container container : containerList) {
      containerMap.put(container.getMeta().getId(), container);
    }

    // if valid container V requires any invalid container, invalidate container V

    containerList
        .stream()
        .filter(Container::isValid)
        .forEach(container -> {

          Set<Dependency> required = container
              .getMeta()
              .getDependencyContainer()
              .getRequired();

          for (Dependency dependency : required) {

            if (!containerMap.get(dependency.getId()).isValid()) {
              container.setValid(false);
              LOG.error(
                  "Valid container [{}] has required dependency on invalid container [{}]",
                  container.getMeta().getId(),
                  dependency.getId()
              );
              break;
            }
          }
        });

    LOG.debug("Leaving getContainerList()");
    LOG.trace("...[{}]", containerList);
    return containerList;
  }
}
