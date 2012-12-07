package com.walkree.gradle.plugin;

import java.io.File;

import com.walkree.gradle.plugin.Target;

/**
 * This class represents a source file in a target.
 */
public class Source {
  // The target to which the source belongs.
  private Target mTarget;

  // The value specified by the user.
  private String mValue;

  /**
   * Create a source file for a given target.
   * @param   target  The given target.
   * @param   value   The value specified by the user.
   */
  public Source(Target target, String value) {
    mTarget = target;
    mValue = value;
  }

  /**
   * Get the {@link File} object associated with the source.
   * @return  The File object.
   */
  public File getFile() {
    return new File(getHomeDirectory(), mValue);
  }

  /**
   * Return the home directory of the package in which this source resides.
   * @return  The home directory of the package in which this source resides.
   */
  public File getHomeDirectory() {
    return mTarget.getHomeDirectory();
  }

  /**
   * Return the building environment in which the source resides.
   * @return  The building environment.
   */
  public Environment getEnvironment() {
    return mTarget.getEnvironment();
  }
}
