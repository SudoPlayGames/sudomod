package com.sudoplay.sudoext.classloader.security;

import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * Created by codetaylor on 2/22/2017.
 */
public class SEServicePolicy extends
    Policy {

  private final IPermissionCollectionProvider applicationPermissionsProvider;
  private final ISandboxPermissionCollectionProvider sandboxPermissionsProvider;

  public SEServicePolicy(
      IPermissionCollectionProvider applicationPermissionsProvider,
      ISandboxPermissionCollectionProvider sandboxPermissionsProvider
  ) {
    this.applicationPermissionsProvider = applicationPermissionsProvider;
    this.sandboxPermissionsProvider = sandboxPermissionsProvider;
  }

  @Override
  public PermissionCollection getPermissions(ProtectionDomain domain) {
    ClassLoader classLoader = domain.getClassLoader();

    if (classLoader instanceof ISandboxClassLoader) {
      return this.sandboxPermissionsProvider.getPermissionCollection(
          ((ISandboxClassLoader) classLoader).getPath()
      );

    } else {
      return this.applicationPermissionsProvider.getPermissionCollection();
    }
  }

}
