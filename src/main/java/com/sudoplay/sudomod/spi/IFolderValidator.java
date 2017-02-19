package com.sudoplay.sudomod.spi;

import java.io.File;

/**
 * Validates the mod location.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public interface IFolderValidator {

  boolean isValidFolder(File modLocation);

}
