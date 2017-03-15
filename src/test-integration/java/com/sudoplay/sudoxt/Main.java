package com.sudoplay.sudoxt;

import com.sudoplay.sudoxt.api.*;
import com.sudoplay.sudoxt.classloader.asm.filter.AllowedJavaUtilClassFilter;
import com.sudoplay.sudoxt.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoxt.classloader.filter.AllowAllClassFilter;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;
import com.sudoplay.sudoxt.classloader.intercept.StaticInjector;
import com.sudoplay.sudoxt.classloader.security.SEServicePolicy;
import com.sudoplay.sudoxt.service.*;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FilePermission;
import java.nio.file.Path;
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
  public void test() throws SXServiceInitializationException, SXPluginException {

    SXService service = new SXServiceBuilder(
        new SXConfigBuilder()
            .setCompressedFileExtension("lsm")
            .setLocations(new Path[]{Paths.get("mods")})
            .setTempLocation(Paths.get("mods-temp"))
            .setMetaFilename("mod-info.json")
            .setApiVersion("1.0"))
        .addStaticInjector(new LoggerStaticInjector())
        .addStaticInjector(new StaticInjector<>(
            IJsonObjectAPIProvider.class,
            container -> new JsonObjectAPIProvider()
        ))
        .addClassLoaderClassFilter(new AllowAllClassFilter())
        //.setCallbackDelegateFactory(NoOpCallbackDelegateFactory.INSTANCE) // testing
        .setByteCodeTransformerBuilder(new SEByteCodeTransformerBuilder()
            //.setByteCodePrinter(new StdOutByteCodePrinter())
            .addClassFilter(new AllowedJavaUtilClassFilter())
            .addClassFilter(new IClassFilter() {
              @Override
              public boolean isAllowed(String name) {
                return name.startsWith("mod.")
                    || name.startsWith("com.sudoplay.math.")
                    || name.startsWith("com.sudoplay.sudoxt.api.");
              }
            })
        )
        .create();

    SXPluginReference<Plugin> pluginA = service.getPlugin("mod_a:mod.ModPlugin", Plugin.class);
    SXPluginReference<Plugin> pluginB = service.getPlugin("mod_b:mod.ModPlugin", Plugin.class);
    SXPluginReference<Plugin> pluginC = service.getPlugin("mod_c:mod.ModPlugin", Plugin.class);
    SXPluginReference<Plugin> pluginD = service.getPlugin("mod_d:mod.ModPlugin", Plugin.class);
    SXPluginReference<Plugin> pluginE = service.getPlugin("mod_e:mod.ModPlugin", Plugin.class);

    List<SXPluginReference<AncillaryPlugin>> referenceList = service.getRegisteredPlugins("blue", AncillaryPlugin.class);

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

    pluginA.invoke(Plugin::onGreeting);
    System.out.println(pluginA.getReport());
    System.out.println("---");

    pluginB.invoke(Plugin::onGreeting);
    System.out.println(pluginB.getReport());
    System.out.println("---");

    this.expect(() -> {
      pluginC.invoke(Plugin::onGreeting);
    });

    for (SXPluginReference<AncillaryPlugin> reference : referenceList) {
      reference.invoke(AncillaryPlugin::doStuff);
    }

    pluginD.invoke(Plugin::onGreeting);
    System.out.println(pluginD.getReport());
    System.out.println("---");

    pluginE.invoke(Plugin::onGreeting);
    System.out.println(pluginE.getReport());
    System.out.println("---");

    service.dispose();
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
