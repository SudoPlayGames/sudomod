package com.sudoplay.sudoext.classloader.security;

import java.security.PermissionCollection;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IPermissionCollectionProvider {

  PermissionCollection getPermissionCollection();

}
