package com.sudoplay.sudoext;

import com.sudoplay.sudoext.api.ModPlugin;
import com.sudoplay.sudoext.classloader.asm.filter.AllowedJavaUtilClassFilter;
import com.sudoplay.sudoext.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoext.classloader.asm.transform.StdOutByteCodePrinter;
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
                return name.startsWith("mod.") || name.startsWith("com.sudoplay.sudoext.api.");
              }
            })
        )

        .create();

    PluginReference<ModPlugin> pluginA = service.getPlugin("test-mod-a:mod.ModPluginA", ModPlugin.class);

    try {

      long start = System.currentTimeMillis();
      pluginA.preLoad();
      System.out.println("Time: " + (System.currentTimeMillis() - start));
      System.out.println("---");

      for (int i = 0; i < 4; i++) {
        pluginA.invoke(ModPlugin::onGreeting);
        System.out.println(pluginA.getReport());
      }

    } catch (PluginException e) {
      LOG.error("", e);

    }

          /*PluginReference<ModPlugin> pluginB = service.getPlugin("test-mod-b:mod.ModPluginB", ModPlugin.class);
      pluginB.invoke(ModPlugin::onGreeting);
      System.out.println(pluginB.getReport());

      PluginReference<ModPlugin> pluginC = service.getPlugin("test-mod-c:mod.ModPluginC", ModPlugin.class);
      pluginC.invoke(ModPlugin::onGreeting);
      System.out.println(pluginC.getReport());

      List<PluginReference<AncillaryPlugin>> list = service.getRegisteredPlugins("blue", AncillaryPlugin.class);

      for (PluginReference<AncillaryPlugin> plugin : list) {
        System.out.println(plugin.invoke(int.class, p -> p.addValues(5, 11)));
        System.out.println(plugin.getReport());
      }*/

    service.dispose();
  }

}
