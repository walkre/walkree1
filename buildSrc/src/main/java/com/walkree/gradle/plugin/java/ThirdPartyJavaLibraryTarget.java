package com.walkree.gradle.plugin.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents third party Java libraries.
 */
public class ThirdPartyJavaLibraryTarget extends Target {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(ThirdPartyJavaLibraryTarget.class);

  /* The user specified path to the jar file. */
  private String mJarPath;

  /**
   * Create a third party Java library target in a given package.
   * @param   pkg   The given package.
   * @param   name  The name of the target.
   */
  public ThirdPartyJavaLibraryTarget(Package pkg, String name) {
    super(pkg, name);
  }

  @Override
  public void initialize() {
    // TODO(jieyu): Fill this function.
    LOGGER.info("ThirdPartyJavaLibraryTarget.initialize()");
  }

  /**
   * Set the path to the jar file.
   * @param   jarPath The path to the jar file.
   */
  public void setJar(String jarPath) {
    mJarPath = jarPath;
  }
}
