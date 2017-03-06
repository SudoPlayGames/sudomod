package com.sudoplay.sudoext;

import com.sudoplay.sudoext.api.ModPlugin;
import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.classloader.asm.filter.AllowedJavaUtilClassFilter;
import com.sudoplay.sudoext.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoext.classloader.filter.AllowAllClassFilter;
import com.sudoplay.sudoext.classloader.filter.IClassFilter;
import com.sudoplay.sudoext.classloader.security.SEServicePolicy;
import com.sudoplay.sudoext.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.security.AllPermission;
import java.security.Permissions;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Main {

  static {
    java.security.Policy.setPolicy(
        new SEServicePolicy(
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

    SEService service = new SEServiceBuilder(new SEConfigBuilder()
        .setCompressedFileExtension(".lsm")
        .setLocation(Paths.get("../sudomod-test/src/mods"))
        .setDataLocation(Paths.get("mod-data"))
        .setTempLocation(Paths.get("mods-temp"))
        .setMetaFilename("mod-info.json")
        .setApiVersion("1.0"))

        //.addClassLoaderClassFilter(new WhitelistClassFilter())
        .addClassLoaderClassFilter(new AllowAllClassFilter())

        .setByteCodeTransformer(new SEByteCodeTransformerBuilder()
            //.setByteCodePrinter(new StdOutByteCodePrinter())
            .addClassFilter(new AllowedJavaUtilClassFilter())
            .addClassFilter(new IClassFilter() {
              @Override
              public boolean isAllowed(String name) {
                return name.startsWith("mod.")
                    || name.startsWith("com.sudoplay.sudoext.api.")
                    || name.startsWith("com.sudoplay.math.");
              }
            })
        )

        .create();

    PluginReference<ModPlugin> pluginA = service.getPlugin("test-mod-a:mod.ModPluginA", ModPlugin.class);
    PluginReference<ModPlugin> pluginB = service.getPlugin("test-mod-b:mod.ModPluginB", ModPlugin.class);
    PluginReference<Plugin> pluginC = service.getPlugin("test-mod-c:mod.ModPluginC", Plugin.class);

    try {

      System.out.println("--- Preload ---");
      service.preload((containerId, resource, percentage, timeMilliseconds, throwable) -> {
        System.out.println(String.format(
            "%s:%s %f %d", containerId, resource, percentage, timeMilliseconds
        ));

        if (throwable != null) {
          throwable.printStackTrace();
        }
      });
      System.out.println("--- End Preload ---");

      for (int i = 0; i < 4; i++) {
        pluginA.invoke(ModPlugin::onGreeting);
      }
      System.out.println(pluginA.getReport());
      System.out.println("---");

      for (int i = 0; i < 4; i++) {
        pluginB.invoke(ModPlugin::onGreeting);
      }
      System.out.println(pluginB.getReport());

    } catch (PluginException e) {
      LOG.error("", e);

    }

    service.disposeFolders();
  }

}
