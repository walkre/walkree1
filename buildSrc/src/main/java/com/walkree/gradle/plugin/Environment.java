package com.walkree.gradle.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import groovy.lang.Closure;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.specs.Spec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents a building environment.
 */
public class Environment {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(Environment.class);

  /* The Gradle project instance. */
  private Project mProject;

  /* A package descriptor index. It records all the packages in the
   * environment. */
  private Map<String, Package> mPackages;

  /**
   * Create a building environment using the given Gradle {@link Project}
   * instance.
   * @param   project   The Gradle project instance.
   */
  public Environment(Project project) {
    mProject = project;
    mPackages = new HashMap<String, Package>();
  }

  /**
   * Configure the building environment. This must be called in order to setup
   * the building environment.
   */
  public void configure() {
    loadPackages();
    configurePackages();
  }

  /**
   * Find the package with the given descriptor. Return null if not found.
   * @param   descriptor  The given package descriptor.
   * @return              The package if found, null otherwise.
   */
  public Package findPackage(String descriptor) {
    return mPackages.get(descriptor);
  }

  /**
   * Find the target with the given descriptor. Return null if not found.
   * @param   descriptor  The given target descriptor.
   * @return              The target if found, null otherwise.
   */
  public Target findTarget(String descriptor) {
    // Since String.split accepts a regular expression, we need to quote the
    // separator in case it is a special character (e.g. '*').
    String[] desc = descriptor.split(Pattern.quote(Constant.TARGET_SEPARATOR));
    if (desc.length != 2) {
      return null;
    }

    Package pkg = findPackage(desc[0]);
    if (pkg == null) {
      return null;
    }

    return pkg.findTarget(descriptor);
  }

  /**
   * Apply a Groovy {@link Closure} on a given object.
   * @param   object  The given object.
   * @param   closure The Groovy closure to be applied.
   * @return          The object after applying the closure.
   */
  public Object applyClosure(Object object, Closure closure) {
    return mProject.configure(object, closure);
  }

  /**
   * Create a Gradle default task with the given name.
   * @param   name  The given task name.
   * @return        The newly created gradle default task.
   */
  public Task createTask(String name) {
    return mProject.task(name);
  }

  /**
   * Return a {@link File} object that represents the root directory of the
   * project.
   * @return  The File object of the project root directory.
   */
  public File getRootDirectory() {
    return mProject.getRootDir();
  }

  /**
   * Abort the building.
   */
  public void abort() {
    System.exit(1);
  }

  /* Load all the necessary packages. */
  private void loadPackages() {
    // TODO(jieyu): Selectively load build files based on the target.
    FileCollection buildFiles = mProject.fileTree(getRootDirectory());
    buildFiles = buildFiles.filter(new Spec<File>() {
      public boolean isSatisfiedBy(File file) {
        return file.isFile() && file.getName().equals("BUILD");
      }
    });

    for (File buildFile : buildFiles.getFiles()) {
      Package pkg = new Package(this, buildFile);
      addPackage(pkg);

      Map<String, Object> options = new HashMap<String, Object>();
      options.put("from", buildFile);
      options.put("to", pkg);
      mProject.apply(options);

      LOGGER.info("Loaded package '{}'", pkg.getDescriptor());
    }
  }

  /* Configure all the loaded packages. */
  private void configurePackages() {
    for (Package pkg : mPackages.values()) {
      pkg.configure();
    }
  }

  /* Add a package to the environment. */
  private void addPackage(Package pkg) {
    mPackages.put(pkg.getDescriptor(), pkg);
  }
}
