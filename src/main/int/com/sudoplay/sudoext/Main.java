package com.sudoplay.sudoext;

import com.sudoplay.sudoext.api.AncillaryPlugin;
import com.sudoplay.sudoext.api.MetaAPI;
import com.sudoplay.sudoext.api.TestModPlugin;
import com.sudoplay.sudoext.api.logging.LoggerFactoryAPI;
import com.sudoplay.sudoext.api.logging.Slf4jLoggerAPIProvider;
import com.sudoplay.sudoext.classloader.intercept.ClassIntercept;
import com.sudoplay.sudoext.config.Config;
import com.sudoplay.sudoext.config.ConfigBuilder;
import com.sudoplay.sudoext.security.Policy;
import com.sudoplay.sudoext.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.security.AllPermission;
import java.security.Permissions;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Main {

  static {
    java.security.Policy.setPolicy(
        new Policy(
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

  public static void main(String... args) throws SEServiceInitializationException {

    Config config = new ConfigBuilder()
        .setFollowLinks(false)
        .setCompressedFileExtension(".lsm")
        .setLocation(Paths.get("../sudomod-test/src/mods"))
        .setDataLocation(Paths.get("mod-data"))
        .setTempLocation(Paths.get("mods-temp"))
        .setMetaFilename("mod-info.json")
        .setApiVersion("1.0")
        .setDefaultMetaApiVersionString("[0,)")
        .addClassFilter(name -> {
          switch (name) {
            case "java.lang.Thread":
              return false;
          }
          return true;
        })
        .addClassIntercept(new ClassIntercept(LoggerFactoryAPI.class, (interceptedClass, container) -> {
          try {
            Field field = interceptedClass.getDeclaredField("LOGGING_API_PROVIDER");
            field.setAccessible(true);
            field.set(null, new Slf4jLoggerAPIProvider(container.getMeta().getId()));

          } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }))
        .addClassIntercept(new ClassIntercept(MetaAPI.class, (interceptedClass, container) -> {
          try {
            Field field = interceptedClass.getDeclaredField("META");
            field.setAccessible(true);
            field.set(null, container.getMeta());

          } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }))
        .getConfig();

    SEService SEService = SEServiceFactory.create(config);
    SEServiceLocator.registerService("default", SEService);

    SEService service = SEServiceLocator.locate("default");

    try {
      ObjectReference<TestModPlugin> pluginA = service.getPlugin("test-mod-a", TestModPlugin.class);
      ObjectReference<TestModPlugin> pluginB = service.getPlugin("test-mod-b", TestModPlugin.class);
      ObjectReference<TestModPlugin> pluginC = service.getPlugin("test-mod-c", TestModPlugin.class);

      ObjectReference<AncillaryPlugin> wrapper = service.get(
          "test-mod-b:scripts.BlueAncillaryPlugin",
          AncillaryPlugin.class
      );

      pluginA.get().onGreeting();
      pluginB.get().onGreeting();
      pluginC.get().onGreeting();

      System.out.println();
      wrapper.get().doStuff();
      System.out.println(wrapper.get().addValues(5, 11));

    } catch (Exception | Error e) {
      LOG.error("", e);
    }

    // TODO: file access
    // TODO: dependency chain testing: C -> B -> A

    service.dispose();
  }

}
