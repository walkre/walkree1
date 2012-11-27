package com.walkree.gradle.plugin.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents Java executables.
 */
public class JavaExecutableTarget extends Target {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaExecutableTarget.class);

  /**
   * Create a java executable target in a given package.
   * @param   pkg   The given package.
   * @param   name  The name of the target.
   */
  public JavaExecutableTarget(Package pkg, String name) {
    super(pkg, name);
  }

  @Override
  public void initialize() {
    // TODO(jieyu): Fill this function.
    LOGGER.info("JavaExecutableTarget.initialize()");
  }
}
