package com.walkree.gradle.plugin;

import com.walkree.gradle.plugin.Target;

/**
 * This class represents a dependency in a target.
 */
public class Dependency {
  /* The target to which this dependency belongs. */
  private Target mTarget;

  /* The value specified by the user. */
  private String mValue;

  /* The dependee target. */
  private Target mDependee;

  /**
   * Create a dependency for a given target.
   * @param   target  The given target.
   * @param   value   The value specified by the user.
   */
  public Dependency(Target target, String value) {
    mTarget = target;
    mValue = value;
  }

  /**
   * Return true if the dependency has been resolved, false otherwise.
   * @return  True if the dependency has been resolved, false otherwise.
   */
  public boolean isResolved() {
    return mDependee != null;
  }

  /**
   * Return the canonicalized descriptor of the dependee target.
   * @return  The canonicalized descriptor of the dependee.
   */
  public String getDependeeDescriptor() {
    // TODO(jieyu): Canonicalize the value (handle relative cases).
    return mValue;
  }

  /**
   * Resolve the dependency by setting the dependee target.
   * @param   dependee  The resolved dependee target.
   */
  public void setDependee(Target dependee) {
    mDependee = dependee;
  }
}
