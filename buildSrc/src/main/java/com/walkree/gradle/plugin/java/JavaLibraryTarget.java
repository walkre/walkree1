package com.walkree.gradle.plugin.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents Java libraries.
 */
public class JavaLibraryTarget extends Target {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaLibraryTarget.class);

  /**
   * Create a Java library target in a given package.
   * @param   pkg   The given package.
   * @param   name  The name of the target.
   */
  public JavaLibraryTarget(Package pkg, String name) {
    super(pkg, name);
  }

  @Override
  public void initialize() {
    // TODO(jieyu): Fill this function.
    LOGGER.info("JavaLibraryTarget.initialize()");
  }
}
