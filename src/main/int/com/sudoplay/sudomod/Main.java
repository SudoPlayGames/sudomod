package com.sudoplay.sudomod;

import com.sudoplay.sudomod.api.AncillaryPlugin;
import com.sudoplay.sudomod.api.TestModPlugin;
import com.sudoplay.sudomod.config.Config;
import com.sudoplay.sudomod.config.ConfigBuilder;
import com.sudoplay.sudomod.mod.security.IClassFilter;
import com.sudoplay.sudomod.mod.security.ModPolicy;
import com.sudoplay.sudomod.service.ModService;
import com.sudoplay.sudomod.service.ModServiceFactory;
import com.sudoplay.sudomod.service.ModServiceInitializationException;
import com.sudoplay.sudomod.service.ModServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.security.AllPermission;
import java.security.Permissions;
import java.security.Policy;
import java.util.Date;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Main {

  static {
    Policy.setPolicy(
        new ModPolicy(
            () -> {
              Permissions permissions = new Permissions();
              permissions.add(new AllPermission());
              return permissions;
            },
            Permissions::new
        )
    );
    System.setSecurityManager(new SecurityManager());
  }

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String... args) throws ModServiceInitializationException {

    Config config = new ConfigBuilder()
        .setFollowLinks(false)
        .setCompressedModFileExtension(".lsm")
        .setModLocation(Paths.get("../sudomod-test/src/mods"))
        .setModDataLocation(Paths.get("mod-data"))
        .setModTempLocation(Paths.get("mods-temp"))
        .setModInfoFilename("mod-info.json")
        .setApiVersion("1.0")
        .setDefaultModInfoApiVersionString("[0,)")
        .setClassFilters(new IClassFilter[]{
            name -> {
              switch (name) {
                case "java.lang.Thread":
                  return false;
              }
              return true;
            }
        })
        .getConfig();

    System.out.println(ModServiceLocator.class);

    ModService modService = ModServiceFactory.create(config);
    ModServiceLocator.registerModService("default", modService);

    ModService service = ModServiceLocator.locate("default");

    try {
      PluginWrapper<TestModPlugin> modPluginA = service.getModPlugin("test-mod-a", TestModPlugin.class);
      PluginWrapper<TestModPlugin> modPluginB = service.getModPlugin("test-mod-b", TestModPlugin.class);

      PluginWrapper<AncillaryPlugin> wrapper = service.get(
          "test-mod-b:scripts.BlueAncillaryPlugin",
          AncillaryPlugin.class
      );

      modPluginA.get().onGreeting();

      while (true) {
        System.out.println();
        System.out.println(new Date());
        service.reload("test-mod-b", true);
        modPluginB.get().onGreeting();

        System.out.println();
        wrapper.get().doStuff();
        System.out.println(wrapper.get().addValues(5, 11));

        Thread.sleep(2000);
      }

    } catch (Exception | Error e) {
      LOG.error("", e);
    }

    // TODO: file access
    // TODO: dependency chain testing: C -> B -> A

    service.dispose();
  }

}
