package com.walkree.gradle.plugin;

import org.gradle.api.Project;

public abstract class Target {
  public static final String DEFAULT_NAME = "NONAME";
  private final Project project;
  private Package pkg;
  private String name;
  private String[] sources;
  private String[] dependencies;

  public Target(Package pkg) {
    this.pkg = pkg;
    this.project = pkg.getProject();
    this.name = DEFAULT_NAME;
  }

  public Package getPackage() {
    return pkg;
  }

  public Project getProject() {
    return project;
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

  public abstract void configure();
}
