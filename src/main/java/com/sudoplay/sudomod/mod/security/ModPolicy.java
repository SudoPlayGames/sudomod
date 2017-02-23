package com.sudoplay.sudomod.mod.security;

import com.sudoplay.sudomod.mod.classloader.IModClassLoader;

import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class ModPolicy extends
    Policy {

  private IPermissionCollectionProvider applicationPermissionsProvider;
  private IPermissionCollectionProvider modPermissionsProvider;

  public ModPolicy(
      IPermissionCollectionProvider applicationPermissionsProvider,
      IPermissionCollectionProvider modPermissionsProvider
  ) {
    this.applicationPermissionsProvider = applicationPermissionsProvider;
    this.modPermissionsProvider = modPermissionsProvider;
  }

  @Override
  public PermissionCollection getPermissions(ProtectionDomain domain) {

    if (this.isMod(domain)) {
      return this.modPermissionsProvider.getPermissionCollection();

    } else {
      return this.applicationPermissionsProvider.getPermissionCollection();
    }
  }

  private boolean isMod(ProtectionDomain domain) {
    return domain.getClassLoader() instanceof IModClassLoader;
  }
}
