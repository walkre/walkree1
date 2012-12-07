package com.walkree.gradle.plugin.common;

import java.io.File;

import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Copy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Job;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents a job that copies files.
 */
public class CopyJob extends Job {
  private static final Logger LOGGER = LoggerFactory.getLogger(CopyJob.class);

  // The files to be copied.
  private FileCollection mFrom;

  // The destination directory.
  private File mDestinationDirectory;

  /**
   * Create a copy job in a given target with the given name.
   * @param   target  The given target.
   * @param   name    The name of the job.
   */
  public CopyJob(Target target, String name) {
    super(target, name);
  }

  @Override
  public CopyJob initialize() {
    return (CopyJob)super.initialize();
  }

  @Override
  public Task createTask() {
    Copy task = getEnvironment().createTask(getTaskName(), Copy.class);

    task.from(mFrom);
    task.into(mDestinationDirectory);

    return task;
  }

  @Override
  public boolean validate() {
    boolean valid = true;

    if (mFrom == null || mFrom.isEmpty()) {
      LOGGER.error("Files to be copied are not specified.");
      valid = false;
    }

    if (mDestinationDirectory == null) {
      LOGGER.error("Destination directory is not specified.");
      valid = false;
    }

    return valid;
  }

  /**
   * Set the files to be copied.
   * @param   from  The files to be copied.
   * @return        This object.
   */
  public CopyJob setFrom(FileCollection from) {
    mFrom = from;
    return this;
  }

  /**
   * Set the destination directory.
   * @param   directory The destination directory.
   * @return            This object.
   */
  public CopyJob setDestinationDirectory(File directory) {
    mDestinationDirectory = directory;
    return this;
  }
}
