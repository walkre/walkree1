package com.walkree.gradle.plugin;

import java.util.List;

import org.gradle.api.Project;
import org.gradle.api.Task;

public abstract class Target {
  public static final String DEFAULT_NAME = "NONAME";
  private final Project project;
  private final BuildSystem buildSystem;
  private Package pkg;
  private String name;
  private String[] sources;
  private String[] dependencies;

  public Target(Package pkg) {
    this.pkg = pkg;
    this.project = pkg.getProject();
    this.buildSystem = pkg.getBuildSystem();
    this.name = DEFAULT_NAME;
  }

  public enum Type {
    CPP_EXECUTABLE, CPP_LIBRARY, JAVA, PYTHON
  }

  public Package getPackage() {
    return pkg;
  }

  public Project getProject() {
    return project;
  }

  public BuildSystem getBuildSystem() {
    return buildSystem;
  }

  public String getName() {
    return name;
  }  

  public void setName(String name) {
    this.name = name;
  }

  public String[] getSources() {
    return sources;
  }

  public void setSources(String[] sources) {    
    this.sources = sources;
  }

  public String[] getDependencies() {
    return dependencies;
  }

  public void setDependencies(String[] dependencies) {
    this.dependencies = dependencies;
  }

  public List<Target> getDependencyTargets() {
    return buildSystem.getDependencyTargets(this);
  }

  public String toString() {
    return getPackage().toString() + ":" + getName();
  }

  public abstract Type getType();

  public abstract void configure();

  public abstract Task getTask();

  public abstract boolean isCompatible(Target depTarget);

  public abstract void dependsOn(Target depTarget);
}
