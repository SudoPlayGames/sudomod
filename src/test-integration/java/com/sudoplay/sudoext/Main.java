package com.sudoplay.sudoext;

import com.sudoplay.sudoext.api.AncillaryPlugin;
import com.sudoplay.sudoext.api.TestModPlugin;
import com.sudoplay.sudoext.classloader.asm.transform.SEByteCodeTransformer;
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
            .setByteCodePrinter(new StdOutByteCodePrinter())
            .addClassFilter(new IClassFilter() {
              @Override
              public boolean isAllowed(String name) {
                return name.startsWith("mod.") || name.startsWith("com.sudoplay.sudoext.api.");
              }
            })
            .create()
        )

        .create();

    try {
      PluginReference<TestModPlugin> pluginA = service.getPlugin("test-mod-a:mod.ModPluginA", TestModPlugin.class);
      //PluginReference<TestModPlugin> pluginB = service.getPlugin("test-mod-b:mod.ModPluginB", TestModPlugin.class);
      //PluginReference<TestModPlugin> pluginC = service.getPlugin("test-mod-c:mod.ModPluginC", TestModPlugin.class);

      /*PluginReference<AncillaryPlugin> wrapper = service.getPlugin(
          "test-mod-b:mod.BlueAncillaryPlugin",
          AncillaryPlugin.class
      );*/

      pluginA.get().onGreeting();
      //pluginB.get().onGreeting();
      //pluginC.get().onGreeting();

      /*System.out.println();
      wrapper.get().doStuff();
      System.out.println(wrapper.get().addValues(5, 11));*/

    } catch (Exception | Error e) {
      LOG.error("", e);
    }

    service.dispose();
  }

}
