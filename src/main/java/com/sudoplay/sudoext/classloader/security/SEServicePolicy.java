package com.sudoplay.sudoext.classloader.security;

import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class SEServicePolicy extends
    Policy {

  private IPermissionCollectionProvider applicationPermissionsProvider;
  private IPermissionCollectionProvider sandboxPermissionsProvider;

  public SEServicePolicy(
      IPermissionCollectionProvider applicationPermissionsProvider,
      IPermissionCollectionProvider sandboxPermissionsProvider
  ) {
    this.applicationPermissionsProvider = applicationPermissionsProvider;
    this.sandboxPermissionsProvider = sandboxPermissionsProvider;
  }

  @Override
  public PermissionCollection getPermissions(ProtectionDomain domain) {

    if (domain.getClassLoader() instanceof ISandboxClassLoader) {
      return this.sandboxPermissionsProvider.getPermissionCollection();

    } else {
      return this.applicationPermissionsProvider.getPermissionCollection();
    }
  }

}
