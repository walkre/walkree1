package com.walkree.gradle.plugin.java;

import java.io.File;

import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.compile.JavaCompile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Job;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents a Java compile job.
 */
public class JavaCompileJob extends Job {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaCompileJob.class);

  // The set of source files.
  private FileCollection mSourceFiles;

  // The Java classpath.
  private FileCollection mClasspath;

  // The destination directory.
  private File mDestinationDirectory;

  // The directory for caching dependencies.
  private File mDependencyCacheDirecotry;

  // The Java source compatibility.
  private String mSourceCompatibility;

  // The Java target compatibility.
  private String mTargetCompatibility;

  /**
   * Create a Java compile job in a given target with the given name.
   * @param   target  The given target.
   * @param   name    The name of the job.
   */
  public JavaCompileJob(Target target, String name) {
    super(target, name);

    mSourceCompatibility = Constant.JAVA_SOURCE_COMPATIBILITY;
    mTargetCompatibility = Constant.JAVA_TARGET_COMPATIBILITY;
  }

  @Override
  public JavaCompileJob initialize() {
    return (JavaCompileJob)super.initialize();
  }

  @Override
  public Task createTask() {
    JavaCompile task = getEnvironment().createTask(getTaskName(),
                                                   JavaCompile.class);

    task.setSource(mSourceFiles);
    task.setClasspath(mClasspath);
    task.setDestinationDir(mDestinationDirectory);
    task.setDependencyCacheDir(mDependencyCacheDirecotry);
    task.setSourceCompatibility(mSourceCompatibility);
    task.setTargetCompatibility(mTargetCompatibility);

    return task;
  }

  @Override
  public boolean validate() {
    boolean valid = true;

    if (mSourceFiles == null || mClasspath.isEmpty()) {
      LOGGER.error("Source files are not specified.");
      valid = false;
    }

    if (mClasspath == null || mClasspath.isEmpty()) {
      LOGGER.error("Classpath is not specified.");
      valid = false;
    }

    if (mDestinationDirectory == null) {
      LOGGER.error("Destination directory is not specified.");
      valid = false;
    }

    if (mDependencyCacheDirecotry == null) {
      LOGGER.error("Dependency cache directory is not specified.");
      valid = false;
    }

    return valid;
  }

  /**
   * Set the set of source files for the job.
   * @param   sourceFiles The set of source files.
   * @return              This object.
   */
  public JavaCompileJob setSourceFiles(FileCollection sourceFiles) {
    mSourceFiles = sourceFiles;
    return this;
  }

  /**
   * Set the classpath for the job.
   * @param   classpath The classpath.
   * @return            This object.
   */
  public JavaCompileJob setClasspath(FileCollection classpath) {
    mClasspath = classpath;
    return this;
  }

  /**
   * Set the destination directory.
   * @param   directory The destination directory.
   * @return            This object.
   */
  public JavaCompileJob setDestinationDirectory(File directory) {
    mDestinationDirectory = directory;
    return this;
  }

  /**
   * Set the directory for caching dependencies.
   * @param   directory The directory for caching dependencies.
   * @return            This object.
   */
  public JavaCompileJob setDependencyCacheDirectory(File directory) {
    mDependencyCacheDirecotry = directory;
    return this;
  }
}
