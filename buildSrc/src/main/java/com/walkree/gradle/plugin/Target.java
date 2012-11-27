package com.walkree.gradle.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Environment;
import com.walkree.gradle.plugin.Dependency;
import com.walkree.gradle.plugin.Job;
import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Source;

/**
 * This class represents a target. A target can be configured in the build file
 * specified by the user.
 */
public abstract class Target {
  private static final Logger LOGGER = LoggerFactory.getLogger(Target.class);

  /* The package in which the target resides. */
  private Package mPackage;

  /* The name of the target. */
  private String mName;

  /* The set of dependencies declared by the target. */
  private Collection<Dependency> mDependencies;

  /* The set of sources declared by the target. */
  private Collection<Source> mSources;

  /* The set of internal jobs in the target. */
  private Collection<Job> mJobs;

  /* The phoney job on which all the internal jobs depend. */
  private Job mStarter;

  /* The phoney job that depends on all the internal jobs. */
  private Job mFinisher;

  /**
   * Create a target in a given package.
   * @param   pkg   The given package.
   * @param   name  The name of the target.
   */
  public Target(Package pkg, String name) {
    mPackage = pkg;
    mName = name;
    mDependencies = new ArrayList<Dependency>();
    mSources = new ArrayList<Source>();
    mJobs = new ArrayList<Job>();
    mStarter = new Job(this, "Starter").initialize();
    mFinisher = new Job(this, "Finisher").initialize();

    // The Finisher always depends on the Starter.
    mFinisher.dependsOn(mStarter);
  }

  /**
   * An abstract initialization function which all subclasses must implement.
   */
  public abstract void initialize();

  /**
   * Configure the target. This must be called in order to setup the target.
   */
  public void configure() {
    initialize();
    resolveDependencies();
  }

  /**
   * Return the canonicalized {@link String} descriptor of the target.
   * @return  The canonicalized descriptor.
   */
  public String getDescriptor() {
    return mPackage.getDescriptor() + Constant.TARGET_SEPARATOR + mName;
  }

  /**
   * Return the building environment in which the target resides.
   * @return  The building environment.
   */
  public Environment getEnvironment() {
    return mPackage.getEnvironment();
  }

  /**
   * Return true if the given dependee can be depended upon by this target,
   * false otherwise.
   * @param   dependee  The given dependee.
   * @return  True if the given dependee can be depended upon, false otherwise.
   */
  protected boolean isValidDependee(Target dependee) {
    return true;
  }

  /**
   * Add an internal job to the target.
   * @param   job   The job to be added.
   */
  protected void addJob(Job job) {
    mJobs.add(job);
    job.dependsOn(mStarter);
    mFinisher.dependsOn(job);
  }

  /**
   * Add a dependency to the target.
   * @param   dependency  The dependency to be added.
   */
  protected void addDependency(Dependency dependency) {
    mDependencies.add(dependency);
  }

  /**
   * Add a source to the target.
   * @param   source  The source to be added.
   */
  protected void addSource(Source source) {
    mSources.add(source);
  }

  /* Resolve all dependencies for this target. */
  private void resolveDependencies() {
    for (Dependency dep : mDependencies) {
      String dependeeDesc= dep.getDependeeDescriptor();
      Target dependee = getEnvironment().findTarget(dependeeDesc);

      if (dependee == null || !isValidDependee(dependee)) {
        LOGGER.error(
            "Failed to resolve a dependency: '{}' -> '{}'.",
            getDescriptor(), dependeeDesc);
        getEnvironment().abort();
      } else {
        dep.setDependee(dependee);
        dependsOn(dependee);
      }
    }
  }

  /* Set the target to be dependent on the given dependee. */
  private void dependsOn(Target dependee) {
    mStarter.dependsOn(dependee.mFinisher);
  }

  /**
   * Configure the dependencies of this target. This function is invoked through
   * the build file specified by the user.
   * @param   values  The list of {@link String} dependency values.
   */
  public void setDependencies(String[] values) {
    for (String value : values) {
      Dependency dep = new Dependency(this, value);
      addDependency(dep);
    }
  }

  /**
   * Configure the sources of this target. This function is invoked through the
   * build file specified by the user.
   * @param   values  The list of {@link String} source values.
   */
  public void setSources(String[] values) {
    for (String value : values) {
      Source src = new Source(this, value);
      addSource(src);
    }
  }
}
