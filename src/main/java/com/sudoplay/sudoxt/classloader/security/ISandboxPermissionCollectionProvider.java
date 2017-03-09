package com.sudoplay.sudoxt.classloader.security;

import java.nio.file.Path;
import java.security.PermissionCollection;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface ISandboxPermissionCollectionProvider {

  PermissionCollection getPermissionCollection(Path path);

}
