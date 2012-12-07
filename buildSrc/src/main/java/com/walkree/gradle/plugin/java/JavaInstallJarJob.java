package com.walkree.gradle.plugin.java;

import java.io.File;

import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Copy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Job;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents a job that installs Java jar files.
 */
public class JavaInstallJarJob extends Job {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaInstallJarJob.class);

  // The set of jar files to be installed.
  private FileCollection mJarFiles;

  // The destination directory.
  private File mDestinationDirectory;

  /**
   * Create a Java install jar job in a given target with the given name.
   * @param   target  The given target.
   * @param   name    The name of the job.
   */
  public JavaInstallJarJob(Target target, String name) {
    super(target, name);
  }

  @Override
  public JavaInstallJarJob initialize() {
    return (JavaInstallJarJob)super.initialize();
  }

  @Override
  public Task createTask() {
    Copy task = getEnvironment().createTask(getTaskName(), Copy.class);

    task.from(mJarFiles);
    task.into(mDestinationDirectory);

    return task;
  }

  @Override
  public boolean validate() {
    boolean valid = true;

    if (mJarFiles == null || mJarFiles.isEmpty()) {
      LOGGER.error("Jar files are not specified.");
      valid = false;
    }

    if (mDestinationDirectory == null) {
      LOGGER.error("Destination directory is not specified.");
      valid = false;
    }

    return valid;
  }

  /**
   * Set the set of jar files to be installed.
   * @param   jarFiles  The set of jar files to be installed.
   * @return            This object.
   */
  public JavaInstallJarJob setJarFiles(FileCollection jarFiles) {
    mJarFiles = jarFiles;
    return this;
  }

  /**
   * Set the destination directory.
   * @param   directory The destination directory.
   * @return            This object.
   */
  public JavaInstallJarJob setDestinationDirectory(File directory) {
    mDestinationDirectory = directory;
    return this;
  }
}
