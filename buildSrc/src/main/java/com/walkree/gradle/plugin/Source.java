package com.walkree.gradle.plugin;

import com.walkree.gradle.plugin.Target;

/**
 * This class represents a source file in a target.
 */
public class Source {
  /* The target to which the source belongs. */
  private Target mTarget;

  /* The value specified by the user. */
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
}
