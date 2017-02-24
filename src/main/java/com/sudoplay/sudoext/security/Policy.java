package com.sudoplay.sudoext.security;

import java.security.PermissionCollection;
import java.security.ProtectionDomain;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class Policy extends
    java.security.Policy {

  private IPermissionCollectionProvider applicationPermissionsProvider;
  private IPermissionCollectionProvider pluginPermissionsProvider;

  public Policy(
      IPermissionCollectionProvider applicationPermissionsProvider,
      IPermissionCollectionProvider pluginPermissionsProvider
  ) {
    this.applicationPermissionsProvider = applicationPermissionsProvider;
    this.pluginPermissionsProvider = pluginPermissionsProvider;
  }

  @Override
  public PermissionCollection getPermissions(ProtectionDomain domain) {

    if (domain.getClassLoader() instanceof ISecureClassLoader) {
      return this.pluginPermissionsProvider.getPermissionCollection();

    } else {
      return this.applicationPermissionsProvider.getPermissionCollection();
    }
  }

}
