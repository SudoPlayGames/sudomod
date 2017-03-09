package com.sudoplay.sudoxt;

import com.sudoplay.sudoxt.api.LoggerStaticInjector;
import com.sudoplay.sudoxt.classloader.asm.filter.AllowedJavaUtilClassFilter;
import com.sudoplay.sudoxt.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoxt.classloader.filter.AllowAllClassFilter;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;
import com.sudoplay.sudoxt.classloader.security.SEServicePolicy;
import com.sudoplay.sudoxt.service.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testapi.AncillaryPlugin;
import testapi.ModPlugin;

import java.io.FilePermission;
import java.nio.file.Paths;
import java.security.AllPermission;
import java.security.Permissions;
import java.security.Policy;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Main {

  static {
    Policy.setPolicy(
        new SEServicePolicy(
            () -> {
              Permissions permissions = new Permissions();
              permissions.add(new AllPermission());
              return permissions;
            },
            path -> {
              Permissions permissions = new Permissions();
              permissions.add(new FilePermission(path.toString() + "/-", "read"));
              return permissions;
            }
        )
    );
    System.setSecurityManager(new SecurityManager());
  }

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  @Test
  public void test() throws SEServiceInitializationException, PluginException {

    SEService service = new SEServiceBuilder(new SEConfigBuilder()
        .setCompressedFileExtension(".lsm")
        .setLocation(Paths.get("../sudomod-test/src/mods"))
        .setDataLocation(Paths.get("mod-data"))
        .setTempLocation(Paths.get("mods-temp"))
        .setMetaFilename("mod-info.json")
        .setApiVersion("1.0"))
        .addStaticInjector(new LoggerStaticInjector())
        .addClassLoaderClassFilter(new AllowAllClassFilter())
        .setByteCodeTransformer(new SEByteCodeTransformerBuilder()
            //.setByteCodePrinter(new StdOutByteCodePrinter())
            .addClassFilter(new AllowedJavaUtilClassFilter())
            .addClassFilter(new IClassFilter() {
              @Override
              public boolean isAllowed(String name) {
                return name.startsWith("mod.")
                    || name.startsWith("com.sudoplay.math.")
                    || name.startsWith("testapi.");
              }
            })
        )
        .create();

    PluginReference<ModPlugin> pluginA = service.getPlugin("test-mod-a:mod.ModPluginA", ModPlugin.class);
    PluginReference<ModPlugin> pluginB = service.getPlugin("test-mod-b:mod.ModPluginB", ModPlugin.class);
    PluginReference<ModPlugin> pluginC = service.getPlugin("test-mod-c:mod.ModPluginC", ModPlugin.class);

    List<PluginReference<AncillaryPlugin>> registeredPlugins = service.getRegisteredPlugins("blue", AncillaryPlugin.class);

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

    pluginA.invoke(ModPlugin::onGreeting);
    System.out.println(pluginA.getReport());
    System.out.println("---");

    pluginB.invoke(ModPlugin::onGreeting);
    System.out.println(pluginB.getReport());

    expect(() -> {
      pluginC.invoke(ModPlugin::onGreeting);
      System.out.println(pluginC.getReport());
    });

    for (PluginReference<AncillaryPlugin> pluginReference : registeredPlugins) {
      Integer integer = pluginReference.invoke(int.class, plugin -> plugin.addValues(42, 73));
      LOG.info("addValues(42, 73): {}", integer);
    }

    service.disposeFolders();
  }

  private interface RunnableException {
    void run() throws Exception;
  }

  private void expect(RunnableException runnable) {

    try {
      runnable.run();
      Assert.fail();

    } catch (Exception e) {
      // expected
      LOG.error(e.getMessage());
    }
  }

}
