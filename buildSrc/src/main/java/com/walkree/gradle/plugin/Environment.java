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
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents a building environment.
 */
public class Environment {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(Environment.class);

  // The Gradle project instance.
  private Project mProject;

  // A package descriptor index. It records all the packages in the environment.
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
   * the building environment. This is the main entry of the plugin.
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
   * Create a Gradle default task with the given name.
   * @param   name  The name of the task.
   * @return        The newly created gradle default task.
   */
  public Task createTask(String name) {
    return mProject.getTasks().add(name);
  }

  /**
   * Create a Gradle task with the given name and type.
   * @param   name  The name of the task.
   * @param   type  The class of the task to be created.
   * @return        The newly created gradle task.
   */
  public <T extends Task> T createTask(String name, Class<T> type) {
    return mProject.getTasks().add(name, type);
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
   * Return a {@link File} object that represents the root directory of the
   * project.
   * @return  The File object of the project root directory.
   */
  public File getRootDirectory() {
    return mProject.getRootDir();
  }

  /**
   * Abort the building. We throw a RuntimeException at the end of this function
   * so that the build system can exit gracefully with pretty prints. We use the
   * logger for the Environment class to log the fatal message, while other
   * messages are logged using the per class loggers.
   * @param   format    The format string passed to the logger.
   * @param   arguments The arguments passed to the logger.
   */
  public void fatal(String format, Object... arguments) {
    // Construct the fatal message body.
    String msg = MessageFormatter.arrayFormat(format, arguments).getMessage();

    // Log the fatal message using the LOGGER for this class.
    Marker fatal = MarkerFactory.getMarker("FATAL");
    LOGGER.error(fatal, msg);

    throw new RuntimeException("FATAL: " + msg);
  }

  /**
   * Resolves a file path relative to the project root directory.
   * @param   path    Any path accepted by file(path) in {@link Project}.
   * @return          The resolved file. Never returns null.
   */
  public File file(Object path) {
    return mProject.file(path);
  }

  /**
   * Returns a {@link FileCollection} instance containing the given files.
   * @param   paths   Any paths accepted by files(paths) in {@link Project}.
   * @return          The file collection. Never returns null.
   */
  public FileCollection files(Object... paths) {
    return mProject.files(paths);
  }

  /**
   * Return a {@link FileCollection} instance containing all files under the
   * given base directory.
   * @param   baseDirectory The base directory of the file tree. Evaluated as
   *                        for file(baseDirectory).
   * @return                The file collection. Never returns null.
   */
  public FileCollection fileTree(Object baseDirectory) {
    return mProject.fileTree(baseDirectory);
  }

  // Load all the necessary packages.
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

  // Configure all the loaded packages.
  private void configurePackages() {
    for (Package pkg : mPackages.values()) {
      pkg.configure();
    }
  }

  // Add a package to the environment.
  private void addPackage(Package pkg) {
    mPackages.put(pkg.getDescriptor(), pkg);
  }
}
