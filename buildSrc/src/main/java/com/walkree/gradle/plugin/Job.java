package com.walkree.gradle.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.Task;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Environment;
import com.walkree.gradle.plugin.Target;

/**
 * This class represents a job. Each job has a Gradle {@link Task} associated
 * with. It represents a unit of work that needs to be performed during
 * building. We say a job depends on other jobs if it cannot be performed until
 * the other jobs are finished.
 */
public class Job {
  // The target to which this job belongs.
  private Target mTarget;

  // The name of the job.
  private String mName;

  // The set of dependents of this job.
  private Collection<Job> mDependents;

  // The Gradle task associated with this job.
  private Task mTask;

  /**
   * Create a job in a given target with the given name.
   * @param   target  The given target.
   * @param   name    The name of the job.
   */
  public Job(Target target, String name) {
    mTarget = target;
    mName = name;
    mDependents = new ArrayList<Job>();
  }

  /**
   * Initialize the job. This function must be called after a Job instance is
   * created.
   */
  public Job initialize() {
    mTask = createTask();
    return this;
  }

  /**
   * Create a Gradle task that is associated with this job. All subclasses must
   * override this function to create job specific tasks.
   * @return  The newly created Gradle task.
   */
  public Task createTask() {
    return getEnvironment().createTask(getTaskName());
  }

  /**
   * Set the job to be dependent on the given dependee job.
   * @param   dependee  The given dependee job.
   */
  public void dependsOn(Job dependee) {
    dependee.mDependents.add(this);
    mTask.dependsOn(dependee.mTask);
  }

  /**
   * Return the building environment in which this job resides.
   * @return  The building environment.
   */
  public Environment getEnvironment() {
    return mTarget.getEnvironment();
  }

  /**
   * Return the canonicalized descriptor of the job.
   * @return  The canonicalized descriptor of the job.
   */
  public String getDescriptor() {
    return mTarget.getDescriptor() + Constant.JOB_SEPARATOR + mName;
  }

  /**
   * Return the name of the Gradle task that is associated with this job.
   * @return  The name of the task.
   */
  protected String getTaskName() {
    // TODO(jieyu): Currently, we cannot use colon in the task name as Gradle
    // will treat it as a project:task separator.
    return getDescriptor().replaceAll(":", "~");
  }
}
