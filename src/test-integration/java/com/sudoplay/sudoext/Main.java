package com.sudoplay.sudoext;

import com.sudoplay.sudoext.api.AncillaryPlugin;
import com.sudoplay.sudoext.api.ModPlugin;
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
            .addClassFilter(new IClassFilter() {
              @Override
              public boolean isAllowed(String name) {
                return name.startsWith("mod.") || name.startsWith("com.sudoplay.sudoext.api.");
              }
            })
        )

        .create();

    try {
      PluginReference<ModPlugin> pluginA = service.getPlugin("test-mod-a:mod.ModPluginA", ModPlugin.class);
      PluginReference<ModPlugin> pluginB = service.getPlugin("test-mod-b:mod.ModPluginB", ModPlugin.class);
      PluginReference<ModPlugin> pluginC = service.getPlugin("test-mod-c:mod.ModPluginC", ModPlugin.class);

      PluginReference<AncillaryPlugin> wrapper = service.getPlugin(
          "test-mod-b:mod.BlueAncillaryPlugin",
          AncillaryPlugin.class
      );

      pluginA.invokeVoid(ModPlugin::onGreeting);
      pluginB.invokeVoid(ModPlugin::onGreeting);
      pluginC.invokeVoid(ModPlugin::onGreeting);

      int result = wrapper.invokeReturn(plugin -> plugin.addValues(5, 11));
      System.out.println(result);

    } catch (Exception | Error e) {
      LOG.error("", e);
    }

    service.dispose();
  }

}
