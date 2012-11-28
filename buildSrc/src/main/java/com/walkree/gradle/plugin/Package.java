package com.walkree.gradle.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import groovy.lang.Closure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Environment;
import com.walkree.gradle.plugin.Target;
import com.walkree.gradle.plugin.java.JavaLibraryTarget;
import com.walkree.gradle.plugin.java.JavaExecutableTarget;
import com.walkree.gradle.plugin.java.ThirdPartyJavaLibraryTarget;

/**
 * This class represents a package. Each package is described using a build
 * file specified by the user.
 */
public class Package {
  private static final Logger LOGGER = LoggerFactory.getLogger(Package.class);

  // The building environment in which the package resides.
  private Environment mEnvironment;

  // The build file that describes the package.
  private File mBuildFile;

  // The home directory of the package.
  private File mHomeDirectory;

  // A target descriptor index. It records all the targets in the package.
  private Map<String, Target> mTargets;

  /**
   * Create a package under a given building environment.
   * @param   environment The given building environment.
   * @param   buildFile   The build file that describes the package.
   */
  public Package(Environment environment, File buildFile) {
    mEnvironment = environment;
    mBuildFile = buildFile;
    mHomeDirectory = buildFile.getParentFile();
    mTargets = new HashMap<String, Target>();
  }

  /**
   * Configure the package. This must be called in order to setup the package.
   */
  public void configure() {
    for (Target target : mTargets.values()) {
      target.configure();
    }
  }

  /**
   * Return the canonicalized {@link String} descriptor of the package.
   * @return  The canonicalized descriptor.
   */
  public String getDescriptor() {
    String homeDirPath = mHomeDirectory.getPath();
    String rootDirPath = getEnvironment().getRootDirectory().getPath();
    return Constant.PROJECT_ROOT_SYMBOL + homeDirPath.substring(rootDirPath.length() + 1);
  }

  /**
   * Find the target with the given descriptor. Return null if not found.
   * @param   descriptor  The given target descriptor.
   * @return              The target if found, null otherwise.
   */
  public Target findTarget(String descriptor) {
    return mTargets.get(descriptor);
  }

  /**
   * Return the building environment in which the package resides.
   * @return  The building environment.
   */
  public Environment getEnvironment() {
    return mEnvironment;
  }

  // Add a target to the package.
  private void addTarget(Target target) {
    mTargets.put(target.getDescriptor(), target);
  }

  /**
   * Add an instance of {@link JavaExecutableTarget} to the package. This
   * function is invoked through the build file specified by the user. The user
   * uses a Groovy {@link Closure} to configure the target.
   * @param   name  The name of the target.
   * @param   closure The Groovy closure used to configure the target.
   */
  public void java_executable(String name, Closure closure) {
    Target target = new JavaExecutableTarget(this, name);
    addTarget(target);

    // Configure the target using the given closure.
    getEnvironment().applyClosure(target, closure);
  }

  /**
   * Add an instance of {@link JavaLibraryTarget} to the package. This function
   * is invoked through the build file specified by the user. The user uses a
   * Groovy {@link Closure} to configure the target.
   * @param   name  The name of the target.
   * @param   closure The Groovy closure used to configure the target.
   */
  public void java_library(String name, Closure closure) {
    Target target = new JavaLibraryTarget(this, name);
    addTarget(target);

    // Configure the target using the given closure.
    getEnvironment().applyClosure(target, closure);
  }

  /**
   * Add an instance of {@link ThirdPartyJavaLibraryTarget} to the package.
   * This function is invoked through the build file specified by the user. The
   * user uses a Groovy {@link Closure} to configure the target.
   * @param   name  The name of the target.
   * @param   closure The Groovy closure used to configure the target.
   */
  public void third_party_java_library(String name, Closure closure) {
    Target target = new ThirdPartyJavaLibraryTarget(this, name);
    addTarget(target);

    // Configure the target using the given closure.
    getEnvironment().applyClosure(target, closure);
  }
}
